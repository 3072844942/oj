package org.oj.server.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.dao.PermissionRepository;
import org.oj.server.dao.UserAuthRepository;
import org.oj.server.dto.Request;
import org.oj.server.entity.Permission;
import org.oj.server.entity.UserAuth;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.JwtUtil;
import org.oj.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * 认证过滤器
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解析token
        String token = request.getHeader("token");
        if (StringUtils.isPresent(token)) {
            String id;
            try {
                id = JwtUtil.parse(token).getSubject();
            } catch (Exception e) {
                throw new ErrorException("token错误，请重新登陆");
            }
            Optional<UserAuth> byId = userAuthRepository.findById(id);
            if (byId.isEmpty()) {
                throw new ErrorException("用户不存在");
            }
            UserAuth userAuth = byId.get();
            if (userAuth.getState().equals(EntityStateEnum.DELETE)) {
                throw new ErrorException("用户被禁用");
            }
            Request.user.set(userAuth);
        }

        // 请求路由
        String uri = request.getRequestURI();
        Optional<Permission> byUrl = permissionRepository.findByUrl(uri);
        if (byUrl.isEmpty()) {
            throw new ErrorException("请求路径不存在");
        }

        Permission permission = byUrl.get();
        // 允许匿名访问
        if (permission.getIsAnonymous()) {
            // 放行
            filterChain.doFilter(request, response);
        }

        if (Request.user.get() == null) {
            throw new ErrorException("请先登陆");
        }

        // 进行授权
        authorizationFilter.doFilterInternal(request, response, filterChain, permission);
    }
}
