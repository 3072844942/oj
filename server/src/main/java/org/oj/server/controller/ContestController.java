package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.annotation.OptLog;
import org.oj.server.constant.OptTypeConst;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.ContestDTO;
import org.oj.server.service.ContestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("contest")
@Tag(name = "比赛接口")
public class ContestController extends BaseController {
    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @Operation(summary = "根据id查找比赛")
    @GetMapping("{contestId}")
    public Object findOne(@PathVariable String contestId) {
        return ok(contestService.findOne(contestId));
    }

    @Operation(summary = "根据id查找比赛题目")
    @GetMapping("{contestId}/{problemId}")
    public Object findOneProblem(@PathVariable String contestId, @PathVariable String problemId) {
        return ok(contestService.findOneProblem(contestId, problemId));
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

    @OptLog(optType = OptTypeConst.UPDATE)
    @Operation(summary = "更新比赛")
    @PatchMapping("update")
    public Object updateOne(@RequestBody ContestDTO contestDTO) {
        return ok(contestService.updateOne(contestDTO));
    }

    @Operation(summary = "报名比赛")
    @PatchMapping("sign/{contestId}")
    public Object signUp(@PathVariable String contestId) {
        return ok(contestService.signUp(contestId));
    }

    @OptLog(optType = OptTypeConst.SAVE)
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

    @OptLog(optType = OptTypeConst.EXPORT)
    @Operation(summary = "导出成绩")
    @GetMapping("export/{contestId}")
    public Object export(@PathVariable String contestId) {
        return ok(contestService.export(contestId));
    }
}
