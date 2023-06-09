package org.oj.server.handler;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.dao.PermissionRepository;
import org.oj.server.dao.UserAuthRepository;
import org.oj.server.dto.Request;
import org.oj.server.entity.Permission;
import org.oj.server.entity.UserAuth;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.service.PermissionService;
import org.oj.server.util.IpUtils;
import org.oj.server.util.JwtUtil;
import org.oj.server.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 认证过滤器
 */
@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
            Optional<UserAuth> byId = userAuthRepository.findById(id);
            if (byId.isEmpty()) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }

            UserAuth userAuth = byId.get();
            if (userAuth.getState().equals(EntityStateEnum.DELETE)) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            }
            Request.user.set(userAuth);

            // 更新用户信息
            String ipAddress = IpUtils.getIpAddress(request);
            userAuth.setIpAddress(ipAddress);
            userAuth.setIpSource(IpUtils.getIpSource(ipAddress));
            userAuth.setLastLoginTime(System.currentTimeMillis());
            userAuthRepository.save(userAuth);
        }

        // 请求路由
        String uri = request.getRequestURI();
        if (!PermissionService.permissionMap.containsKey(uri)) {
            throw new ErrorException(StatusCodeEnum.SYSTEM_ERROR);
        }

        Permission permission = PermissionService.permissionMap.get(uri);
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
