package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.FriendLinkDTO;
import org.oj.server.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:19
 */
@RestController
@RequestMapping("link")
@Tag(name = "友链接口")
public class FriendLinkController extends BaseController {
    private final FriendLinkService friendLinkService;

    public FriendLinkController(FriendLinkService friendLinkService) {
        this.friendLinkService = friendLinkService;
    }

    @Operation(summary = "查找友链列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return friendLinkService.find(conditionDTO);
    }

    @Operation(summary = "更新友链")
    @PatchMapping("update")
    public Object updateOne(@RequestBody FriendLinkDTO friendDTO) {
        return ok(friendLinkService.updateOne(friendDTO));
    }

    @Operation(summary = "插入友链")
    @PutMapping("add")
    public Object insertOne(@RequestBody FriendLinkDTO friendDTO) {
        return ok(friendLinkService.insertOne(friendDTO));
    }

    @Operation(summary = "批量删除友链")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        friendLinkService.delete(ids);
        return ok();
    }

    @Operation(summary = "删除友链")
    @DeleteMapping("delete/{friendId}")
    public Object deleteOne(@PathVariable String friendId) {
        friendLinkService.deleteOne(friendId);
        return ok();
    }
}
