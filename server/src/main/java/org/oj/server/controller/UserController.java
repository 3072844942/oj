package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.UserInfoDTO;
import org.oj.server.dto.UsernamePassword;
import org.oj.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * todo 管理用户
 * @author march
 * @since 2023/5/31 下午3:20
 */
@RestController
@RequestMapping("user")
@Tag(name = "用户接口")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户登陆")
    @PostMapping("login")
    public Object login(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.login(usernamePassword));
    }

    @Operation(summary = "发送验证码")
    @PostMapping("code")
    public Object sendCode(@RequestBody UsernamePassword usernamePassword) {
        userService.sendCode(usernamePassword);
        return ok();
    }

    @Operation(summary = "注册")
    @PostMapping("register")
    public Object register(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.register(usernamePassword));
    }

    @Operation(summary = "忘记密码")
    @PostMapping("forget")
    public Object forget(@RequestBody UsernamePassword usernamePassword) {
        return ok(userService.forget(usernamePassword));
    }

    @Operation(summary = "修改信息")
    @PatchMapping("update")
    public Object updateOne(@RequestBody UserInfoDTO userInfoDTO) {
        return ok(userService.updateOne(userInfoDTO));
    }

    @Operation(summary = "查找用户")
    @GetMapping("list")
    public Object find(@RequestBody ConditionDTO conditionDTO) {
        return ok(userService.find(conditionDTO));
    }
}
