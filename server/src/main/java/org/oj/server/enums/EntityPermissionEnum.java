package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 权限等级枚举
 * 查看权限
 *
 * @author march
 * @since 2023/5/31 上午11:32
 */
@Getter
@AllArgsConstructor
public enum EntityPermissionEnum {
    /**
     * 仅自己
     */
    PRIVATE(0, "仅自己", null),

    /**
     * 指定角色
     * 使用时必须赋值!!!
     */
    PROTECTED(1, "指定角色", null),

    /**
     * 公开
     */
    PUBLIC(2, "公开", null);

    /**
     * 编号
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 指定组
     */
    private List<String> roleId;

    public static EntityPermissionEnum valueOf(Integer permission) {
        EntityPermissionEnum[] values = EntityPermissionEnum.values();
        for (EntityPermissionEnum i : values) {
            if (i.getCode().equals(permission)) {
                return i;
            }
        }
        return EntityPermissionEnum.PRIVATE;
    }

    public EntityPermissionEnum setRole(List<String> roleId) {
        this.roleId = roleId;
        return this;
    }
}
