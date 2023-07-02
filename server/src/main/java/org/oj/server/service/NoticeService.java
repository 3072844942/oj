package org.oj.server.service;

import org.oj.server.constant.HtmlConst;
import org.oj.server.constant.MongoConst;
import org.oj.server.dao.NoticeRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.NoticeDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Article;
import org.oj.server.entity.Notice;
import org.oj.server.entity.User;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.NoticeSearchVO;
import org.oj.server.vo.NoticeVO;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:12
 */
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    public NoticeService(NoticeRepository noticeRepository, UserService userService, MongoTemplate mongoTemplate) {
        this.noticeRepository = noticeRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    public NoticeDTO insertOne(NoticeDTO noticeDTO) {
        NoticeDTO.check(noticeDTO);

        // id不为空
        if (StringUtils.isPresent(noticeDTO.getId())) {
            // 数据已存在
            if (noticeRepository.existsById(noticeDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            noticeDTO.setId(null);
        }

        Notice notice = Notice.of(noticeDTO);
        // 设置作者
        if (Request.user.get() != null) {
            notice.setUserId(Request.user.get().getId());
        }
        notice.setState(EntityStateEnum.DRAFT);
        // 没有写权限
        if (!PermissionUtil.enableWrite("")) {
            notice.setIsTop(false);
        }

        notice = noticeRepository.insert(notice);

        return NoticeDTO.of(notice);
    }

    public NoticeDTO updateOne(NoticeDTO noticeDTO) {
        NoticeDTO.check(noticeDTO);

        // id为空
        if (StringUtils.isEmpty(noticeDTO.getId())) {
            throw new WarnException(StatusCodeEnum.FAILED_PRECONDITION);
        }
        Optional<Notice> byId = noticeRepository.findById(noticeDTO.getId());
        // 数据不存在
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        // 不是自己的， 或者没有写权限
        if (!PermissionUtil.enableWrite(byId.get().getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Notice notice = byId.get();
        notice.setUserId(Request.user.get().getId());
        if (StringUtils.isPresent(noticeDTO.getCover())) notice.setCover(noticeDTO.getCover());
        if (StringUtils.isPresent(noticeDTO.getTitle())) notice.setTitle(noticeDTO.getTitle());
        if (StringUtils.isPresent(noticeDTO.getContent())) notice.setContent(noticeDTO.getContent());
        if (PermissionUtil.enableWrite("")) {
            notice.setIsTop(noticeDTO.getIsTop());
            notice.setState(EntityStateEnum.valueOf(noticeDTO.getState()));
        } else {
            notice.setIsTop(false);
            // 没有权限不允许立刻公开
            if (!EntityStateEnum.PUBLIC.equals(EntityStateEnum.valueOf(noticeDTO.getState())))
                notice.setState(EntityStateEnum.valueOf(noticeDTO.getState()));
        }
        // 保存
        notice = noticeRepository.save(notice);
        return NoticeDTO.of(notice);
    }

    public void deleteOne(String id) {
        Notice notice = findById(id);

        // 不是自己的， 或没有写权限
        if (!PermissionUtil.enableWrite(notice.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        noticeRepository.deleteById(id);
    }

    public void delete(List<String> ids) {
        // 批量删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        noticeRepository.deleteAllById(ids);
    }

    public NoticeVO findOne(String id) {
        Notice notice = findById(id);
        // 无读权限
        if (!PermissionUtil.enableRead(notice.getState(), notice.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        NoticeVO noticeVO = NoticeVO.of(notice);

        noticeVO.setAuthor(UserProfileVO.of(userService.findById(notice.getUserId())));

        return noticeVO;
    }

    private Notice findById(String id) {
        // id为空
        if (StringUtils.isEmpty(id)) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        Optional<Notice> byId = noticeRepository.findById(id);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        return byId.get();
    }

    public PageVO<NoticeSearchVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        // 有读写权限
        if (PermissionUtil.enableRead(EntityStateEnum.DRAFT, "")) {
            // 随意读
            query.addCriteria(Criteria.where(MongoConst.STATE).is(EntityStateEnum.valueOf(conditionDTO.getState())));
        } else {
            EntityStateEnum state = EntityStateEnum.valueOf(conditionDTO.getState());
            // 如果读的不是公开
            if (!state.equals(EntityStateEnum.PUBLIC)) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            } else {
                query.addCriteria(Criteria.where(MongoConst.STATE).is(state));
            }
        }

        // 指定了作者
        if (conditionDTO.getId() != null) {
            query.addCriteria(Criteria.where(MongoConst.USER_ID).is(conditionDTO.getId()));
        }
        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(MongoConst.TITLE).regex(keywords),
                    Criteria.where(MongoConst.CONTENT).regex(keywords)
            ));
        }

        long count = mongoTemplate.count(query, Notice.class);

        query.with(Sort.by(Sort.Order.desc(MongoConst.UPDATE_TIME)));
        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Notice> all = mongoTemplate.find(query, Notice.class);

        return new PageVO<>(
                // 设置高亮
                parse(all.stream().peek(notice -> {
                    if (keywords != null) {
                        notice.setTitle(notice.getTitle().replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG));
                        notice.setContent(StringUtils.subKeywords(notice.getContent(), keywords));
                    }
                }).toList()),
                count
        );
    }

    /**
     * 转换数据对象
     *
     * @param all
     * @return
     */
    private List<NoticeSearchVO> parse(List<Notice> all) {
        List<String> userIds = all.stream().map(Notice::getUserId).toList();
        Map<String, User> infoMap = userService.findAllById(userIds);

        return all.stream()
                .map(a -> {
                    NoticeSearchVO noticeSearchDTO = NoticeSearchVO.of(a);

                    noticeSearchDTO.setAuthor(UserProfileVO.of(infoMap.get(a.getUserId())));
                    return noticeSearchDTO;
                })
                .toList();
    }
}
