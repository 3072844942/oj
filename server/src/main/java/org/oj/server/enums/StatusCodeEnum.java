package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口状态码枚举
 *
 * @author yezhqiu
 * @date 2021/06/11
 **/
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    SUCCESS(0, "操作成功"),
    UNAUTHORIZED(1, "没有操作权限"),
    SYSTEM_ERROR(2, "系统异常"),
    FAIL(3, "操作失败"),
    FAILED_PRECONDITION(4, "参数格式不正确"),
    DATA_EXIST(5, "数据已存在"),
    DATA_NOT_EXIST(6, "数据不存在"),
    LOGIN_ERROR(7, "登陆错误"),
    PASSWORD_NOT_MATCHED(8, " 密码错误");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

}
