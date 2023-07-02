package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import org.oj.server.dao.TagRepository;
import org.oj.server.dao.TagRepository;
import org.oj.server.dto.TagDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Tag;
import org.oj.server.entity.Tag;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.TagVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final MongoTemplate mongoTemplate;

    public TagService(TagRepository tagRepository, MongoTemplate mongoTemplate) {
        this.tagRepository = tagRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public TagVO insertOne(TagDTO tagDTO) {
        TagDTO.check(tagDTO);

        // id不为空
        if (StringUtils.isPresent(tagDTO.getId())) {
            // 数据已存在
            if (tagRepository.existsById(tagDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            tagDTO.setId("");
        }

        Tag tag = Tag.of(tagDTO);
        tag = tagRepository.insert(tag);

        return TagVO.of(tag);
    }

    public void delete(List<String> ids) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        tagRepository.deleteAllById(ids);
    }

    public void deleteOne(String id) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        tagRepository.deleteById(id);
    }

    public PageVO<TagVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(Criteria.where("name").regex(keywords));
        }

        long count = mongoTemplate.count(query, Tag.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Tag> categories = mongoTemplate.find(query, Tag.class);
        return new PageVO<>(
                categories.stream().map(TagVO::of).toList(),
                count
        );
    }

    public TagDTO updateOne(TagDTO tagDTO) {
        TagDTO.check(tagDTO);

        // 数据已存在
        if (!tagRepository.existsById(tagDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Tag tag = Tag.of(tagDTO);
        tag = tagRepository.insert(tag);

        return TagDTO.of(tag);
    }
}
