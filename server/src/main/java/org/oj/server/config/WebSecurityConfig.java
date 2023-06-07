package org.oj.server.config;

import org.oj.server.handler.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
//        registry.addInterceptor(authenticationFilter);
    }
}
