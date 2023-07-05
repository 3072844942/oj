package org.oj.server.service;

import org.oj.server.dao.CommentRepository;
import org.oj.server.dto.CommentDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Comment;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.MongoTemplateUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.CommentVO;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.UserProfileVO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    private final MongoTemplateUtils mongoTemplateUtils;

    public CommentService(CommentRepository commentRepository, UserService userService, MongoTemplateUtils mongoTemplateUtils) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.mongoTemplateUtils = mongoTemplateUtils;
    }

    public PageVO<CommentVO> find(String articleId) {
        List<Comment> all = commentRepository.findAllByArticleId(articleId);

        Set<String> userIds = all.stream().map(Comment::getUserId).collect(Collectors.toSet());
        userIds.addAll(all.stream().map(Comment::getReplyUserId).toList());
        Map<String, UserProfileVO> infoMap = userService.findAllById(userIds);

        List<CommentVO> list = all.stream().map(c -> {
            CommentVO commentVO = CommentVO.of(c);
            commentVO.setAuthor(infoMap.get(c.getUserId()));
            commentVO.setReplyUser(infoMap.get(c.getReplyUserId()));
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

    public CommentVO verify(String commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Comment comment = byId.get();

        // 不是待审核的
        if (!comment.getState().equals(EntityStateEnum.REVIEW)) {
            throw new ErrorException(StatusCodeEnum.FAIL);
        }

        // 设置公开
        comment.setState(EntityStateEnum.PUBLIC);
        comment = commentRepository.save(comment);

        CommentVO commentVO = CommentVO.of(comment);
        commentVO.setAuthor(UserProfileVO.of(userService.findById(comment.getUserId())));
        commentVO.setReplyUser(UserProfileVO.of(userService.findById(comment.getReplyUserId())));
        return commentVO;
    }

    public CommentVO insertOne(CommentDTO commentDTO) {
        CommentDTO.check(commentDTO);

        Comment comment = Comment.of(commentDTO);
        comment.setId(null);

        // 设置作者
        if (Request.user.get() != null) {
            comment.setUserId(Request.user.get().getId());
        }
        comment.setState(EntityStateEnum.REVIEW);

        comment = commentRepository.insert(comment);

        CommentVO commentVO = CommentVO.of(comment);
        commentVO.setAuthor(UserProfileVO.of(userService.findById(comment.getUserId())));
        commentVO.setReplyUser(UserProfileVO.of(userService.findById(comment.getReplyUserId())));
        return commentVO;
    }

    public void deleteOne(String commentId) {
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        mongoTemplateUtils.delete(commentId, Comment.class, byId.get().getUserId());
    }

    public void delete(List<String> ids) {
        mongoTemplateUtils.delete(ids, Comment.class);
    }
}
