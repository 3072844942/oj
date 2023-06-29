package org.oj.server.service;

import org.oj.server.dao.FriendLinkRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.FriendLinkDTO;
import org.oj.server.entity.FriendLink;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.FriendLinkVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class FriendLinkService {
    private final FriendLinkRepository friendLinkRepository;
    private final MongoTemplate mongoTemplate;

    public FriendLinkService(FriendLinkRepository friendLinkRepository, MongoTemplate mongoTemplate) {
        this.friendLinkRepository = friendLinkRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public FriendLinkVO insertOne(FriendLinkDTO friendLinkDTO) {
        FriendLinkDTO.check(friendLinkDTO);

        // id不为空
        if (StringUtils.isPresent(friendLinkDTO.getId())) {
            // 数据已存在
            if (friendLinkRepository.existsById(friendLinkDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            friendLinkDTO.setId("");
        }

        FriendLink friendLink = FriendLink.of(friendLinkDTO);
        friendLink = friendLinkRepository.insert(friendLink);

        return FriendLinkVO.of(friendLink);
    }

    public void delete(List<String> ids) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        friendLinkRepository.deleteAllById(ids);
    }

    public void deleteOne(String id) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        friendLinkRepository.deleteById(id);
    }

    public PageVO<FriendLinkVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("name").regex(keywords),
                    Criteria.where("url").regex(keywords),
                    Criteria.where("desc").regex(keywords)
            ));
        }

        long count = mongoTemplate.count(query, FriendLink.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<FriendLink> categories = mongoTemplate.find(query, FriendLink.class);
        return new PageVO<>(
                categories.stream().map(FriendLinkVO::of).toList(),
                count
        );
    }

    public FriendLinkDTO updateOne(FriendLinkDTO friendLinkDTO) {
        FriendLinkDTO.check(friendLinkDTO);

        // 数据已存在
        if (!friendLinkRepository.existsById(friendLinkDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        FriendLink friendLink = FriendLink.of(friendLinkDTO);
        friendLink = friendLinkRepository.insert(friendLink);

        return FriendLinkDTO.of(friendLink);
    }
}
