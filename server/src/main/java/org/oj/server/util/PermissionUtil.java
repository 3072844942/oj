package org.oj.server.util;

import org.oj.server.dto.Request;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;

/**
 * @author march
 * @since 2023/6/6 下午3:00
 */
public class PermissionUtil {
    /**
     * 是否允许读
     *       写    读    无
     * 公开   Y     Y    Y
     * 其他   Y     Y    N
     * @param state 实体状态
     * @return 读状态
     */
    public static boolean enableRead(EntityStateEnum state, String authorId) {
        PermissionEnum permission = Request.permission.get();
        // 已删除的禁止读
        if (state.equals(EntityStateEnum.DELETE)) return false;
        // 角色具有 读 / 写权限
        if (permission.equals(PermissionEnum.WRITE) || permission.equals(PermissionEnum.READ)) return true;
        // 是本人的
        if (Request.user.get() != null && Request.user.get().getId().equals(authorId)) return true;
        // 实体是公开
        return state.equals(EntityStateEnum.PUBLIC);
    }

    /**
     * 是否允许写
     * @return 写状态
     */
    public static boolean enableWrite(String authorId) {
        // 是本人的
        if (Request.user.get() != null && Request.user.get().getId().equals(authorId)) return true;
        // 角色具有写权限
        return Request.permission.get().equals(PermissionEnum.WRITE);
    }
}
