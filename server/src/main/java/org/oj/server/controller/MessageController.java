package org.oj.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.MessageDTO;
import org.oj.server.service.MessageService;
import org.springframework.web.bind.annotation.*;

/**
 * @author march
 * @since 2023/7/2 下午5:29
 */
@RestController
@RequestMapping("message")
@Tag(name = "留言接口")
public class MessageController extends BaseController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("list")
    public Object list() {
        return ok(messageService.findAll());
    }

    @PutMapping("add")
    public Object insertOne(@RequestBody MessageDTO messageDTO) {
        return ok(messageService.insertOne(messageDTO));
    }

    @DeleteMapping("delete/{messageId}")
    public Object deleteOne(@PathVariable String messageId) {
        messageService.deleteOne(messageId);
        return ok();
    }
}
