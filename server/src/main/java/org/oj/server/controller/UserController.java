package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.UsernamePassword;
import org.oj.server.service.UserAuthService;
import org.oj.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author march
 * @since 2023/5/31 下午3:20
 */
@RestController
@RequestMapping("user")
@Tag(name = "用户接口")
public class UserController extends BaseController {
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "用户登陆")
    @PostMapping("login")
    public Object login(@RequestBody UsernamePassword usernamePassword) {
        return ok(userAuthService.login(usernamePassword));
    }

    @Operation(summary = "发送验证码")
    @PostMapping("code")
    public Object sendCode(@RequestBody UsernamePassword usernamePassword) {
        userAuthService.sendCode(usernamePassword);
        return ok();
    }

    @Operation(summary = "注册")
    @PostMapping("register")
    public Object register(@RequestBody UsernamePassword usernamePassword) {
        return ok(userAuthService.register(usernamePassword));
    }
}
