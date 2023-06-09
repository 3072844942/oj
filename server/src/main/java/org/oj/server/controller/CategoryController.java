package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("category")
@Tag(name = "分类接口")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "查找分类列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return categoryService.find(conditionDTO);
    }

    @PatchMapping("update")
    public Object updateOne(@RequestBody CategoryDTO categoryDTO) {
        return ok(categoryService.updateOne(categoryDTO));
    }

    @Operation(summary = "插入分类")
    @PutMapping("add")
    public Object insertOne(@RequestBody CategoryDTO categoryDTO) {
        return ok(categoryService.insertOne(categoryDTO));
    }

    @Operation(summary = "批量删除分类")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        categoryService.delete(ids);
        return ok();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("delete/{categoryId}")
    public Object deleteOne(@PathVariable String categoryId) {
        categoryService.deleteOne(categoryId);
        return ok();
    }
}
