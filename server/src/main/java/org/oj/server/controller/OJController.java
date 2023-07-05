package org.oj.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.jni.JudgeJNIService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author march
 * @since 2023/6/9 上午9:36
 */
@RestController
@Tag(name = "OJ接口")
public class OJController extends BaseController {
    @GetMapping("/hello")
    public Object hello() {
        return Map.of("hello", new Date().toString());
    }

    @GetMapping("/hello/judge")
    public Object judgeHello() {
        return Map.of("hello", JudgeJNIService.hello());
    }
}
