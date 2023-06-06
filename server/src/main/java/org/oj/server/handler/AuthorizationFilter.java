package org.oj.server.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.dao.RoleRepository;
import org.oj.server.dto.Request;
import org.oj.server.entity.Permission;
import org.oj.server.entity.Role;
import org.oj.server.entity.UserAuth;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 授权拦截器
 *
 * @author march
 * @since 2023/6/2 下午5:31
 */
@Component
public class AuthorizationFilter {
    @Autowired
    private RoleRepository roleRepository;

    public boolean doFilterInternal(HttpServletRequest request, HttpServletResponse response, Permission permission) throws ServletException, IOException {
        UserAuth userAuth = Request.user.get();
        List<String> roleIds = userAuth.getRoleIds();

        AtomicBoolean flag = new AtomicBoolean(false);
        roleIds.forEach(id -> {
            Optional<Role> byId = roleRepository.findById(id);
            if (byId.isEmpty()) return;

            Role role = byId.get();
            Map<String, PermissionEnum> map = role.getPermissionIds();
            // 如果包含该权限
            if (map.containsKey(permission.getId())) {
                flag.set(true);
                Request.permission.set(map.get(permission.getId()));
            }
        });

        if (!flag.get()) {
            // 无操作权限
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        // 放行
        return true;
    }
}
