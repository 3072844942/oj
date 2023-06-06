package org.oj.server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.oj.server.handler.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springSecurity
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOriginPatterns("*").allowedMethods("*");
    }

    /**
     * 配置权限
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationFilter);
    }



//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // 前后端分离是无状态的，不需要session了，直接禁用。
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
////                        // 允许所有OPTIONS请求
////                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
////                        // 允许直接访问授权登录接口
////                        .requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
////                        // 允许 SpringMVC 的默认错误地址匿名访问
////                        .requestMatchers("/error").permitAll()
//                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
//                        //.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
//                        // 允许任意请求被已登录用户访问，不检查Authority
//                        .anyRequest().permitAll())
//                // 添加过滤器 认证
//                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // 省略HttpSecurity的配置
//        return http.build();
//    }
}
