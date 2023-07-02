package org.oj.server.handler;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.dao.PermissionRepository;
import org.oj.server.dao.UserRepository;
import org.oj.server.dto.Request;
import org.oj.server.entity.Permission;
import org.oj.server.entity.User;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.IpUtils;
import org.oj.server.util.JwtUtil;
import org.oj.server.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 认证过滤器
 */
@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {
    private final UserRepository userRepository;
    private final AuthorizationFilter authorizationFilter;
    private final PermissionRepository permissionRepository;

    private final List<String> swaggerList = Arrays.asList("/v3", "/error", "/favicon.ico", "/webjars", "/doc.html", "/assets");

    public JwtAuthenticationFilter(UserRepository userRepository, AuthorizationFilter authorizationFilter, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.authorizationFilter = authorizationFilter;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 解析token
        String token = request.getHeader("token");
        if (StringUtils.isPresent(token)) {
            Claims claims;
            try {
                claims = JwtUtil.parse(token);
            } catch (Exception e) {
                throw new ErrorException(StatusCodeEnum.LOGIN_ERROR);
            }

            // 签证过期
            if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
                throw new ErrorException(StatusCodeEnum.LOGIN_ERROR);
            }

            String id = claims.getId();
            Optional<User> byId = userRepository.findById(id);
            if (byId.isEmpty()) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }

            User user = byId.get();
            if (user.getState().equals(EntityStateEnum.DELETE)) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            }

            // 更新用户信息
            String ipAddress = IpUtils.getIpAddress(request);
            user.setIpAddress(ipAddress);
            user.setIpSource(IpUtils.getIpSource(ipAddress));
            userRepository.save(user);
            Request.user.set(user);
        }

        // 请求路由
        String uri = request.getRequestURI();

        // swagger允许访问
        if (swaggerList.stream().anyMatch(uri::contains)) {
            return true;
        }
        Optional<Permission> byUrl = permissionRepository.findByUrl(uri);
        if (byUrl.isEmpty()) {
            throw new ErrorException("请求路径错误：" + uri);
        }

        Permission permission = byUrl.get();
        // 允许匿名访问
        if (permission.getIsAnonymous()) {
            // 放行
            return true;
        }

        if (Request.user.get() == null) {
            throw new ErrorException(StatusCodeEnum.LOGIN_ERROR);
        }

        // 进行授权
        return authorizationFilter.doFilterInternal(request, response, permission);
    }
}
