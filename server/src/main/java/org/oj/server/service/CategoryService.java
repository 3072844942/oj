package org.oj.server.service;

import org.oj.server.constant.MongoConst;
import org.oj.server.dao.CategoryRepository;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Category;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.MongoTemplateUtils;
import org.oj.server.util.QueryUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.CategoryVO;
import org.oj.server.vo.PageVO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:07
 */
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;

    private final MongoTemplateUtils mongoTemplateUtils;

    public CategoryService(CategoryRepository categoryRepository, MongoTemplate mongoTemplate, MongoTemplateUtils mongoTemplateUtils) {
        this.categoryRepository = categoryRepository;
        this.mongoTemplate = mongoTemplate;
        this.mongoTemplateUtils = mongoTemplateUtils;
    }

    public CategoryVO insertOne(CategoryDTO categoryDTO) {
        CategoryDTO.check(categoryDTO);

        // id不为空
        if (StringUtils.isPresent(categoryDTO.getId())) {
            // 数据已存在
            if (categoryRepository.existsById(categoryDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            categoryDTO.setId("");
        }

        Category category = Category.of(categoryDTO);
        category = categoryRepository.insert(category);

        return CategoryVO.of(category);
    }

    public void delete(List<String> ids) {
        mongoTemplateUtils.delete(ids, Category.class);
    }

    public void deleteOne(String id) {
        mongoTemplateUtils.delete(id, Category.class);
    }

    public PageVO<CategoryVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(Criteria.where(MongoConst.TITLE).regex(keywords));
        }

        long count = mongoTemplate.count(query, Category.class);

        QueryUtils.skip(query, conditionDTO);
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return new PageVO<>(
                categories.stream().map(CategoryVO::of).toList(),
                count
        );
    }

    public CategoryDTO updateOne(CategoryDTO categoryDTO) {
        CategoryDTO.check(categoryDTO);

        // 数据不存在
        if (!categoryRepository.existsById(categoryDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Category category = Category.of(categoryDTO);
        category = categoryRepository.insert(category);

        return CategoryDTO.of(category);
    }
}
