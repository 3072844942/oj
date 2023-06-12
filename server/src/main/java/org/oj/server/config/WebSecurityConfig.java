package org.oj.server.config;

import org.oj.server.handler.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    /**
     * 允许所有访问
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOriginPatterns("*").allowedMethods("*");
    }

    /**
     * 配置权限
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 认证拦截器
        registry.addInterceptor(authenticationFilter);
    }
}
