package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import org.oj.server.dao.CategoryRepository;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Article;
import org.oj.server.entity.Category;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.CategoryVO;
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
 * @since 2023/5/31 下午3:07
 */
@Service
public class CategoryService {
    public static final Map<String, Category> categoryMap = new HashMap<>();
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

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
        categoryMap.put(category.getId(), category);

        return CategoryVO.of(category);
    }

    public void delete(List<String> ids) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        categoryRepository.deleteAllById(ids);
        ids.forEach(categoryMap::remove);
    }

    public void deleteOne(String id) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        categoryRepository.deleteById(id);
        categoryMap.remove(id);
    }

    public PageVO<CategoryVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(Criteria.where("title").regex(keywords));
        }

        long count = mongoTemplate.count(query, Category.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return new PageVO<>(
                categories.stream().map(CategoryVO::of).toList(),
                count
        );
    }

    public CategoryDTO updateOne(CategoryDTO categoryDTO) {
        CategoryDTO.check(categoryDTO);

        // 数据已存在
        if (!categoryRepository.existsById(categoryDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Category category = Category.of(categoryDTO);
        category = categoryRepository.insert(category);
        categoryMap.put(category.getId(), category);

        return CategoryDTO.of(category);
    }

    @PostConstruct
    private void init() {
        // 预读取所有分类， 减少查询时间
        List<Category> all = categoryRepository.findAll();
        all.forEach(category -> categoryMap.put(category.getId(), category));
    }
}
