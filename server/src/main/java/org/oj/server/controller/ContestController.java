package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.oj.server.dto.ContestDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("contest")
public class ContestController extends BaseController {
    @Autowired
    private ContestService contestService;

    @Operation(summary = "根据id查找比赛")
    @GetMapping("{contestId}")
    public Object findOne(@PathVariable String contestId) {
        return ok(contestService.findOne(contestId));
    }

    @Operation(summary = "根据id查找比赛")
    @GetMapping("{contestId}/rank")
    public Object findOneRank(@PathVariable String contestId, ConditionDTO conditionDTO) {
        return ok(contestService.findOneRank(contestId, conditionDTO));
    }

    @Operation(summary = "根据id查找比赛")
    @GetMapping("{contestId}/user")
    public Object findOneUser(@PathVariable String contestId, ConditionDTO conditionDTO) {
        return ok(contestService.findOneUser(contestId, conditionDTO));
    }

    @Operation(summary = "查找比赛列表")
    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(contestService.find(conditionDTO));
    }

    @Operation(summary = "更新比赛")
    @PatchMapping("update")
    public Object updateOne(@RequestBody ContestDTO contestDTO) {
        return ok(contestService.updateOne(contestDTO));
    }

    @Operation(summary = "发布比赛")
    @PatchMapping("publish/{contestId}")
    public Object publishOne(@PathVariable String contestId) {
        return ok(contestService.publishOne(contestId));
    }

    @Operation(summary = "隐藏比赛")
    @PatchMapping("hide/{contestId}")
    public Object hideOne(@PathVariable String contestId) {
        return ok(contestService.hideOne(contestId));
    }

    @Operation(summary = "审核比赛")
    @PatchMapping("verify/{contestId}")
    public Object verifyOne(@PathVariable String contestId) {
        return ok(contestService.verifyOne(contestId));
    }

    @Operation(summary = "回收比赛")
    @PatchMapping("recycle/{contestId}")
    public Object recycleOne(@PathVariable String contestId) {
        return ok(contestService.recycleOne(contestId));
    }

    @Operation(summary = "恢复比赛到草稿")
    @PatchMapping("recover/{contestId}")
    public Object recoverOne(@PathVariable String contestId) {
        return ok(contestService.hideOne(contestId));
    }
    @Operation(summary = "报名比赛")
    @PatchMapping("sign/{contestId}")
    public Object signUp(@PathVariable String contestId) {
        return ok(contestService.signUp(contestId));
    }

    @Operation(summary = "添加比赛")
    @PutMapping("add")
    public Object insertOne(@RequestBody ContestDTO contestDTO) {
        return ok(contestService.insertOne(contestDTO));
    }

    @Operation(summary = "删除比赛")
    @DeleteMapping("delete/{contestId}")
    public Object deleteOne(@PathVariable String contestId) {
        contestService.deleteOne(contestId);
        return ok();
    }

    @Operation(summary = "批量删除比赛")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        contestService.delete(ids);
        return ok();
    }
}
