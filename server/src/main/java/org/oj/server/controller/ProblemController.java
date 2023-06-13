package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ProblemDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 题目管理
 * 评测交给Judge服务
 *
 * @author march
 * @since 2023/5/31 下午3:19
 */
@RestController
@RequestMapping("problem")
@Tag(name = "题目接口")
public class ProblemController extends BaseController {
    @Autowired
    private ProblemService problemService;

    @Operation(summary = "根据id查找题目")
    @GetMapping("{problemId}")
    public Object findOne(@PathVariable String problemId) {
        return ok(problemService.findOne(problemId));
    }

    @Operation(summary = "查找题目列表")
    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(problemService.find(conditionDTO));
    }

    @Operation(summary = "更新题目")
    @PatchMapping("update")
    public Object updateOne(@RequestBody ProblemDTO problemDTO) {
        return ok(problemService.updateOne(problemDTO));
    }

    @Operation(summary = "发布题目")
    @PatchMapping("publish/{problemId}")
    public Object publishOne(@PathVariable String problemId) {
        return ok(problemService.publishOne(problemId));
    }

    @Operation(summary = "隐藏题目")
    @PatchMapping("hide/{problemId}")
    public Object hideOne(@PathVariable String problemId) {
        return ok(problemService.hideOne(problemId));
    }

    @Operation(summary = "回收题目")
    @PatchMapping("recycle/{problemId}")
    public Object recycleOne(@PathVariable String problemId) {
        return ok(problemService.recycleOne(problemId));
    }

    @Operation(summary = "恢复题目到草稿")
    @PatchMapping("recover/{problemId}")
    public Object recoverOne(@PathVariable String problemId) {
        return ok(problemService.hideOne(problemId));
    }

    @Operation(summary = "添加题目")
    @PutMapping("add")
    public Object insertOne(@RequestBody ProblemDTO problemDTO) {
        return ok(problemService.insertOne(problemDTO));
    }

    @Operation(summary = "删除题目")
    @DeleteMapping("delete/{problemId}")
    public Object deleteOne(@PathVariable String problemId) {
        problemService.deleteOne(problemId);
        return ok();
    }

    @Operation(summary = "批量删除题目")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        problemService.delete(ids);
        return ok();
    }
}
