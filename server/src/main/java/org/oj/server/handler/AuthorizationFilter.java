package org.oj.server.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.dao.RoleRepository;
import org.oj.server.dto.Request;
import org.oj.server.entity.Permission;
import org.oj.server.entity.Role;
import org.oj.server.entity.User;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 授权拦截器
 *
 * @author march
 * @since 2023/6/2 下午5:31
 */
@Component
public class AuthorizationFilter {
    private final RoleRepository roleRepository;

    public AuthorizationFilter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean doFilterInternal(HttpServletRequest request, HttpServletResponse response, Permission permission) {
        User userAuth = Request.user.get();
        List<String> roleIds = userAuth.getRoleIds();

        List<Role> allById = roleRepository.findAllById(roleIds);
        allById.forEach(role -> {
            Map<String, PermissionEnum> map = role.getPermissionIds();
            // 如果包含该权限
            if (map.containsKey(permission.getId())) {
                // 选出更大的权限
                Request.permission.set(
                        PermissionEnum.max(Request.permission.get(),
                                map.get(permission.getId()))
                );
            }
        });

        if (Request.permission.get() == null) {
            // 无操作权限
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        // 放行
        return true;
    }
}
