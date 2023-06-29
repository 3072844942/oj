package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.TagDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:20
 */
@RestController
@RequestMapping("tag")
@Tag(name = "标签接口")
public class TagController extends BaseController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "查找分类列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return tagService.find(conditionDTO);
    }

    @Operation(summary = "更新分类")
    @PatchMapping("update")
    public Object updateOne(@RequestBody TagDTO tagDTO) {
        return ok(tagService.updateOne(tagDTO));
    }

    @Operation(summary = "插入分类")
    @PutMapping("add")
    public Object insertOne(@RequestBody TagDTO tagDTO) {
        return ok(tagService.insertOne(tagDTO));
    }

    @Operation(summary = "批量删除分类")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        tagService.delete(ids);
        return ok();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("delete/{tagId}")
    public Object deleteOne(@PathVariable String tagId) {
        tagService.deleteOne(tagId);
        return ok();
    }
}
