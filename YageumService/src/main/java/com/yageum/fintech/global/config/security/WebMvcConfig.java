package com.yageum.fintech.global.config.security;

import com.yageum.fintech.global.config.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;
    private final Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] activeProfiles = environment.getActiveProfiles();
        if(activeProfiles != null && activeProfiles.length > 0 && !activeProfiles[0].equals("test"))
            registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
                    .excludePathPatterns("/api/v1/auth/**");
    }
}