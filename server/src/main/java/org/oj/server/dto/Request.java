package org.oj.server.dto;

import org.oj.server.entity.User;
import org.oj.server.enums.PermissionEnum;

/**
 * 请求会话信息
 *
 * @author march
 * @since 2023/6/2 下午5:46
 */
public class Request {
    /**
     * 请求的用户
     */
    public static ThreadLocal<User> user = new ThreadLocal<>();

    /**
     * 请求的角色权限
     */
    public static ThreadLocal<PermissionEnum> permission = new ThreadLocal<>();
}
