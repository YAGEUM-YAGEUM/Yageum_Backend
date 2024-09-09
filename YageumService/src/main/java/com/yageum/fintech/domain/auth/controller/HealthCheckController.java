package com.yageum.fintech.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health_check")
    public String healthCheck() {
        // 헬스 체크 정보를 문자열로 생성
        StringBuilder healthStatus = new StringBuilder();
        healthStatus.append("Status: UP\n");
        healthStatus.append("Message: Service is running smoothly");

        return healthStatus.toString(); // 텍스트 형식으로 반환
    }
}

