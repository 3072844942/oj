package org.oj.server.service;

import org.oj.server.dao.CommentRepository;
import org.oj.server.dao.UserInfoRepository;
import org.oj.server.dto.CommentDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Comment;
import org.oj.server.entity.UserInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.CommentVO;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    //todo
    public PageVO<CommentVO> find(String articleId) {
        Comment comment = Comment.builder()
                .articleId(articleId)
                .state(EntityStateEnum.PUBLIC)
                .build();

        List<Comment> all = commentRepository.findAll(Example.of(comment));

        List<String> userIds = new ArrayList<>(all.stream().map(Comment::getUserId).toList());
        userIds.addAll(all.stream().map(Comment::getReplyUserId).toList());
        List<UserInfo> infos = userInfoRepository.findAllById(userIds);
        Map<String, UserInfo> infoMap = infos.stream().collect(Collectors.toMap(UserInfo::getId, a -> a, (k1, k2) -> k1));

        List<CommentVO> list = all.stream().map(c -> {
            CommentVO commentVO = CommentVO.of(c);
            commentVO.setAuthor(UserProfileVO.of(infoMap.get(c.getUserId())));
            commentVO.setReplyUser(UserProfileVO.of(infoMap.get(c.getReplyUserId())));
            return commentVO;
        }).toList();

        // 根据parentId 分类集合
        Map<String, List<Comment>> commentMap = all.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Comment::getParentId));

        list.forEach(i -> i.getChildren().addAll(commentMap.get(i.getId()).stream().map(CommentVO::of).toList()));

        return new PageVO<>(
                list.stream().filter(commentVO -> commentVO.getReplyUser() == null).toList(),
                (long) all.size());
    }

    public CommentDTO verify(String commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Comment comment = byId.get();

        // 不是待审核的
        if (!comment.getState().equals(EntityStateEnum.REVIEW)) {
            throw new WarnException(StatusCodeEnum.FAIL);
        }

        // 设置公开
        comment.setState(EntityStateEnum.PUBLIC);
        comment = commentRepository.save(comment);

        return CommentDTO.of(comment);
    }

    public CommentVO insertOne(CommentDTO commentDTO) {
        CommentDTO.check(commentDTO);

        // id不为空
        if (StringUtils.isPresent(commentDTO.getId())) {
            // 数据已存在
            if (commentRepository.existsById(commentDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            commentDTO.setId(null);
        }

        Comment comment = Comment.of(commentDTO);
        // 设置作者
        if (Request.user.get() != null) {
            comment.setUserId(Request.user.get().getId());
        }
        comment.setState(EntityStateEnum.REVIEW);

        comment = commentRepository.insert(comment);

        CommentVO commentVO = CommentVO.of(comment);
        commentVO.setAuthor(UserProfileVO.of(userInfoRepository.findById(comment.getUserId()).get()));
        commentVO.setReplyUser(UserProfileVO.of(userInfoRepository.findById(comment.getReplyUserId()).get()));
        return commentVO;
    }

    public void deleteOne(String commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        if (!PermissionUtil.enableWrite(byId.get().getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        commentRepository.deleteById(commentId);
    }

    public void delete(List<String> ids) {
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        commentRepository.deleteAllById(ids);
    }
}
