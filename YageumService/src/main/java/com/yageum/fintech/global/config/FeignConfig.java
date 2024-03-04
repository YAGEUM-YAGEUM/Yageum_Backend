package com.yageum.fintech.global.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 5000);
    }
}
