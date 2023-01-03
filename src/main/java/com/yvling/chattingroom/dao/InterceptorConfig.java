package com.yvling.chattingroom.dao;

import com.yvling.chattingroom.interceptor.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration registration = registry.addInterceptor(new Interceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/login", "/register", "/**/login.html", "/**/register.html", "/**/*.css", "/**/*.js", "/**/*.png", "/");
    }
}
