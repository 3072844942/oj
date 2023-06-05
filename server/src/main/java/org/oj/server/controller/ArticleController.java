package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @Operation(summary = "查找首页文章列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return ok(articleService.find(conditionDTO));
    }

    @PatchMapping("update")
    public Object updateOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.updateOne(articleDTO));
    }

    @PutMapping("add")
    public Object insertOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.insertOne(articleDTO));
    }

    @DeleteMapping("delete")
    public Object deleteOne(@RequestBody ArticleDTO articleDTO) {
        return ok(articleService.deleteOne(articleDTO));
    }
}
