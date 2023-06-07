package org.oj.server.service;

import org.oj.server.dao.CategoryRepository;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Category;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.CategoryVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author march
 * @since 2023/5/31 下午3:07
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

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
        // 批量删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        categoryRepository.deleteAllById(conditionDTO.getIds());
    }

    public void deleteOne(ConditionDTO conditionDTO) {
        categoryRepository.deleteById(conditionDTO.getId());
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
}
