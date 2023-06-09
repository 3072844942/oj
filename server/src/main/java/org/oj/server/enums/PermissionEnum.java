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
    /**
     * 仅自己
     */
    NONE(0, "无权限"),
    /**
     * 可读所有
     */
    READ(1, "读全部"),
    /**
     * 可全部操作
     */
    WRITE(2, "写全部")
    ;
    private final Integer code ;
    private final String desc;

    public static PermissionEnum max(PermissionEnum p1, PermissionEnum p2) {
        int p = p1.getCode().compareTo(p2.getCode());
        if (p > 0) return p1;
        else if (p == 0) return p1;
        else return p2;
    }
}
