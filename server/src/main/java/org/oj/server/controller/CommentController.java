package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.CommentDTO;
import org.oj.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("comment")
@Tag(name = "文章评论")
public class CommentController extends BaseController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(description = "查询评论列表")
    @GetMapping("{articleId}")
    public Object list(@PathVariable String articleId) {
        return ok(commentService.find(articleId));
    }

    @Operation(description = "审核评论")
    @PatchMapping("verify/{commentId}")
    public Object verify(@PathVariable String commentId) {
        return ok(commentService.verify(commentId));
    }

    @Operation(description = "添加评论")
    @PutMapping("add")
    public Object insertOne(@RequestBody CommentDTO commentDTO) {
        return ok(commentService.insertOne(commentDTO));
    }

    @Operation(description = "删除评论")
    @DeleteMapping("delete/{commentId}")
    public Object deleteOne(@PathVariable String commentId) {
        commentService.deleteOne(commentId);
        return ok();
    }

    @Operation(description = "批量删除评论")
    @DeleteMapping("delete/list")
    public Object deleteOne(@RequestBody List<String> ids) {
        commentService.delete(ids);
        return ok();
    }
}
