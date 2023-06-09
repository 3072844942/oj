package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.annotation.OptLog;
import org.oj.server.constant.OptTypeConst;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.UserInfoDTO;
import org.oj.server.dto.UsernamePassword;
import org.oj.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 管理用户
 * @author march
 * @since 2023/5/31 下午3:20
 */
@RestController
@RequestMapping("user")
@Tag(name = "用户接口")
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @OptLog(optType = OptTypeConst.SAVE)
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

    @OptLog(optType = OptTypeConst.UPDATE)
    @Operation(summary = "修改信息")
    @PatchMapping("update")
    public Object updateOne(@RequestBody UserInfoDTO userInfoDTO) {
        return ok(userService.updateOne(userInfoDTO));
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @Operation(summary = "实名认证")
    @PatchMapping("certificate")
    public Object certificate(@RequestBody UserInfoDTO userInfoDTO) {
        userService.certificateOne(userInfoDTO);
        return ok();
    }

    @Operation(summary = "查找用户")
    @GetMapping("list")
    public Object find(@RequestBody ConditionDTO conditionDTO) {
        return ok(userService.find(conditionDTO));
    }
}
