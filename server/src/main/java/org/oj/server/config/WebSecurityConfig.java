package org.oj.server.config;


import org.oj.server.handler.AuthenticationFilter;
import org.oj.server.handler.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * springSecurity
 */
@Configuration
public class WebSecurityConfig {
    @Autowired
    private AuthenticationFilter authenticationFilter;
    @Autowired
    private AuthorizationFilter authorizationFilter;

    /**
     * 配置权限
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 添加过滤器 认证
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 省略HttpSecurity的配置
        return http.build();
    }
}
