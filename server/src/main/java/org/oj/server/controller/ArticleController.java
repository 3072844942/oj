package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "查找文章")
    @GetMapping("find")
    public Object findOne(ConditionDTO conditionDTO) {
        return ok(articleService.findOne(conditionDTO));
    }

    @Operation(summary = "查找首页文章列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return ok(articleService.find(conditionDTO));
    }

    @Operation(summary = "查找草稿文章列表")
    @PostMapping("list/draft")
    public Object findDraft(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.findDraft(conditionDTO));
    }

    @Operation(summary = "查找回收站文章列表")
    @PostMapping("list/recycle")
    public Object findRecycle(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.findRecycle(conditionDTO));
    }

    @Operation(summary = "查找待审核文章列表")
    @PostMapping("list/review")
    public Object findReview(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.findReview(conditionDTO));
    }

    @Operation(summary = "查找公开文章列表")
    @PostMapping("list/public")
    public Object findPublic(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.find(conditionDTO));
    }

    @Operation(summary = "更新文章")
    @PatchMapping("update")
    public Object updateOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.updateOne(articleDTO));
    }

    @Operation(summary = "发布文章")
    @PatchMapping("publish")
    public Object publishOne(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.publishOne(conditionDTO));
    }

    @Operation(summary = "隐藏文章")
    @PatchMapping("hide")
    public Object hideOne(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.hideOne(conditionDTO));
    }

    @Operation(summary = "审核文章")
    @PatchMapping("verify")
    public Object verifyOne(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.verifyOne(conditionDTO));
    }

    @Operation(summary = "回收文章")
    @PatchMapping("recycle")
    public Object recycleOne(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.recycleOne(conditionDTO));
    }

    @Operation(summary = "恢复文章到草稿")
    @PatchMapping("recover")
    public Object recoverOne(@RequestBody ConditionDTO conditionDTO) {
        return ok(articleService.recover(conditionDTO));
    }

    @Operation(summary = "添加文章")
    @PutMapping("add")
    public Object insertOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.insertOne(articleDTO));
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("delete")
    public Object deleteOne(@RequestBody ConditionDTO conditionDTO) {
        articleService.deleteOne(conditionDTO);
        return ok();
    }

    @Operation(summary = "批量删除文章")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody ConditionDTO conditionDTO) {
        articleService.delete(conditionDTO);
        return ok();
    }
}
