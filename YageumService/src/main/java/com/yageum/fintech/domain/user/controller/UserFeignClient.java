package com.yageum.fintech.domain.user.controller;

import com.yageum.fintech.domain.user.dto.GetUserResponseDto;
import com.yageum.fintech.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignConfig.class)
public interface UserFeignClient {
    @GetMapping("/response_userById/{userId}")
    ResponseEntity<GetUserResponseDto> getUser(@PathVariable("userId") Long userId);
}
