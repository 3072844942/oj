package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色权限枚举
 *
 * @author march
 * @since 2023/6/5 下午3:20
 */
@Getter
@AllArgsConstructor
public enum PermissionEnum {
    NONE(0, "无权限"),
    READ(1, "读全部"),
    WRITE(2, "写全部")
    ;
    private final Integer code ;
    private final String desc;
}
