package org.oj.server.service;

import org.oj.server.dao.CategoryRepository;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Article;
import org.oj.server.entity.Category;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.ArticleSearchDTO;
import org.oj.server.vo.CategoryVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public CategoryVO insertOne(CategoryDTO categoryDTO) {
        WarnException checked = CategoryDTO.check(categoryDTO);
        if (checked != null) {
            throw checked;
        }

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

    public void delete(ConditionDTO conditionDTO) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        categoryRepository.deleteAllById(conditionDTO.getIds());
    }

    public void deleteOne(String id) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        categoryRepository.deleteById(id);
    }

    public PageVO<CategoryVO> find(ConditionDTO conditionDTO) {
        WarnException checked = ConditionDTO.check(conditionDTO);
        if (checked != null) {
            throw checked;
        }

        Page<Category> all = categoryRepository.findAll(PageRequest.of(conditionDTO.getCurrent() - 1, conditionDTO.getSize()));
        long count = categoryRepository.count();

        return new PageVO<>(all.map(CategoryVO::of).toList(), count);
    }

    public PageVO<CategoryVO> find(CategoryDTO categoryDTO, ConditionDTO conditionDTO) {
        WarnException checked = ConditionDTO.check(conditionDTO);
        if (checked != null) {
            throw checked;
        }

        // 查询条件
        Query query = new Query();
        if (categoryDTO != null) {
            if (categoryDTO.getTitle() != null) query.addCriteria(Criteria.where("title").regex(categoryDTO.getTitle()));
        }

        long count = mongoTemplate.count(query, Article.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Category> categories = mongoTemplate.find(query, Category.class);
        return new PageVO<>(
                categories.stream().map(CategoryVO::of).toList(),
                count
        );
    }
}
