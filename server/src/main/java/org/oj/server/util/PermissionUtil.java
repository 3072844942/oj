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
     * 写    读    无
     * 公开   Y     Y    Y
     * 其他   Y     Y    N
     *
     * @param state 实体状态
     * @return 读状态
     */
    public static boolean enableRead(EntityStateEnum state, String authorId) {
        // 已删除的禁止读
        if (state.equals(EntityStateEnum.DELETE)) return false;
        // 是本人的
        if (Request.user.get() != null && Request.user.get().getId().equals(authorId)) return true;

        PermissionEnum permission = Request.permission.get();
        // 角色具有 读 / 写权限
        if (permission != null) {
            if (permission.equals(PermissionEnum.WRITE) || permission.equals(PermissionEnum.READ)) return true;
        }
        // 实体是公开
        return state.equals(EntityStateEnum.PUBLIC);
    }

    /**
     * 是否允许写
     *
     * @return 写状态
     */
    public static boolean enableWrite(String authorId) {
        // 是本人的
        if (Request.user.get() != null && Request.user.get().getId().equals(authorId)) return true;
        // 角色具有写权限
        PermissionEnum permissionEnum = Request.permission.get();
        return permissionEnum != null && permissionEnum.equals(PermissionEnum.WRITE);
    }

    /**
     * 检查实体的状态
     *
     * @param entity      实体
     * @param authorId    实体的作者
     * @param targetState 目标状态
     */
    public static void checkState(StateEnable entity, String authorId, Integer targetState) {
        if (enableWrite(authorId)) {
            entity.setState(EntityStateEnum.valueOf(targetState));
        } else {
            // 没有权限不允许立刻公开
            if (EntityStateEnum.PUBLIC.equals(EntityStateEnum.valueOf(targetState))) {
                entity.setState(EntityStateEnum.REVIEW);
            } else {
                entity.setState(EntityStateEnum.valueOf(targetState));
            }
        }
    }

    public static void checkTop(TopEnable entity, Boolean targetIsTop) {
        // 有写权限
        if (enableWrite("")) {
            entity.setIsTop(targetIsTop);
        } else {
            entity.setIsTop(false);
        }
    }
}
