package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Autowired
    private NoticeService noticeService;

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

    @Operation(summary = "更新公告")
    @PatchMapping("update")
    public Object updateOne(@RequestBody NoticeDTO noticeDTO) {
        return ok(noticeService.updateOne(noticeDTO));
    }

    @Operation(summary = "发布公告")
    @PatchMapping("publish/{noticeId}")
    public Object publishOne(@PathVariable String noticeId) {
        return ok(noticeService.publishOne(noticeId));
    }

    @Operation(summary = "隐藏公告")
    @PatchMapping("hide/{noticeId}")
    public Object hideOne(@PathVariable String noticeId) {
        return ok(noticeService.hideOne(noticeId));
    }

    @Operation(summary = "回收公告")
    @PatchMapping("recycle/{noticeId}")
    public Object recycleOne(@PathVariable String noticeId) {
        return ok(noticeService.recycleOne(noticeId));
    }

    @Operation(summary = "恢复公告到草稿")
    @PatchMapping("recover/{noticeId}")
    public Object recoverOne(@PathVariable String noticeId) {
        return ok(noticeService.hideOne(noticeId));
    }

    @Operation(summary = "添加公告")
    @PutMapping("add")
    public Object insertOne(@RequestBody NoticeDTO noticeDTO) {
        return ok(noticeService.insertOne(noticeDTO));
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("delete/{noticeId}")
    public Object deleteOne(@PathVariable String noticeId) {
        noticeService.deleteOne(noticeId);
        return ok();
    }

    @Operation(summary = "批量删除公告")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        noticeService.delete(ids);
        return ok();
    }
}
