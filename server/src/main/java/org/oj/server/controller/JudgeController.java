package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.JudgeDTO;
import org.oj.server.dto.PoolInfoDTO;
import org.oj.server.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评测
 * @author march
 * @since 2023/6/12 上午10:16
 */
@RestController
@Tag(name = "评测接口")
@RequestMapping("judge")
public class JudgeController extends BaseController {
    private final JudgeService judgeService;

    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @Operation(summary = "评测")
    @PostMapping("")
    public Object judge(@RequestBody JudgeDTO judgeDTO) {
        return ok(judgeService.judge(judgeDTO));
    }

    @Operation(summary = "自测")
    @PostMapping("debug")
    public Object debug(@RequestBody JudgeDTO judgeDTO) {
        return ok(judgeService.debug(judgeDTO));
    }

    @Operation(summary = "测试代码")
    @PostMapping("test")
    public Object test(@RequestBody JudgeDTO judgeDTO) {
        return ok(judgeService.test(judgeDTO));
    }

    @Operation(summary = "更新线程池信息")
    @PatchMapping("pool")
    public Object updatePool(@RequestBody PoolInfoDTO poolInfoDTO) {
        return ok(judgeService.updatePool(poolInfoDTO));
    }

    @Operation(summary = "获取线程池信息")
    @GetMapping("pool")
    public Object getPool() {
        return ok(judgeService.getPool());
    }

    @Operation(summary = "查看提交记录")
    @GetMapping("list")
    public Object listRecord(ConditionDTO conditionDTO) {
        return ok(judgeService.listRecord(conditionDTO));
    }
}
