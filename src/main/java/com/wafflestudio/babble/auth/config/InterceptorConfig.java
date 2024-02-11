package com.wafflestudio.babble.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wafflestudio.babble.auth.application.JwtTokenService;
import com.wafflestudio.babble.auth.presentation.AuthInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final JwtTokenService jwtTokenService;

    public InterceptorConfig(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtTokenService))
            .addPathPatterns("/api/chat/**");
    }
}
