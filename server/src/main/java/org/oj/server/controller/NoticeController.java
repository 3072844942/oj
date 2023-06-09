package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.annotation.OptLog;
import org.oj.server.constant.OptTypeConst;
import org.oj.server.dto.NoticeDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:19
 */
@RestController
@RequestMapping("notice")
@Tag(name = "公告接口")
public class NoticeController extends BaseController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "根据id查找公告")
    @GetMapping("{noticeId}")
    public Object findOne(@PathVariable String noticeId) {
        return ok(noticeService.findOne(noticeId));
    }

    @Operation(summary = "查找公告列表")
    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(noticeService.find(conditionDTO));
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @Operation(summary = "更新公告")
    @PatchMapping("update")
    public Object updateOne(@RequestBody NoticeDTO noticeDTO) {
        return ok(noticeService.updateOne(noticeDTO));
    }

    @OptLog(optType = OptTypeConst.SAVE)
    @Operation(summary = "添加公告")
    @PutMapping("add")
    public Object insertOne(@RequestBody NoticeDTO noticeDTO) {
        return ok(noticeService.insertOne(noticeDTO));
    }

    @OptLog(optType = OptTypeConst.REMOVE)
    @Operation(summary = "删除公告")
    @DeleteMapping("delete/{noticeId}")
    public Object deleteOne(@PathVariable String noticeId) {
        noticeService.deleteOne(noticeId);
        return ok();
    }

    @OptLog(optType = OptTypeConst.REMOVE)
    @Operation(summary = "批量删除公告")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        noticeService.delete(ids);
        return ok();
    }
}
