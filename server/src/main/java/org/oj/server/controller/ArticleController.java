package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:17
 */
@RestController
@RequestMapping("article")
@Tag(name = "文章接口")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;

    @Operation(summary = "根据id查找文章")
    @GetMapping("{articleId}")
    public Object findOne(@PathVariable String articleId) {
        return ok(articleService.findOne(articleId));
    }

    @Operation(summary = "查找文章列表")
    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(articleService.find(conditionDTO));
    }

    @Operation(summary = "更新文章")
    @PatchMapping("update")
    public Object updateOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.updateOne(articleDTO));
    }

    @Operation(summary = "添加文章")
    @PutMapping("add")
    public Object insertOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.insertOne(articleDTO));
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("delete/{articleId}")
    public Object deleteOne(@PathVariable String articleId) {
        articleService.deleteOne(articleId);
        return ok();
    }

    @Operation(summary = "批量删除文章")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        articleService.delete(ids);
        return ok();
    }
}
