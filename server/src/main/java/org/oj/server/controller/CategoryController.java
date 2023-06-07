package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("article")
@Tag(name = "分类接口")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "查找分类")
    @GetMapping
    public Object find(ConditionDTO conditionDTO) {
        return categoryService.find(conditionDTO);
    }

    @Operation(summary = "插入分类")
    @PutMapping("add")
    public Object insertOne(@RequestBody CategoryDTO categoryDTO) {
        return ok(categoryService.insertOne(categoryDTO));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("delete")
    public Object deleteOne(@RequestBody ConditionDTO conditionDTO) {
        categoryService.deleteOne(conditionDTO);
        return ok();
    }

    @Operation(summary = "批量删除分类")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody ConditionDTO conditionDTO) {
        categoryService.delete(conditionDTO);
        return ok();
    }
}
