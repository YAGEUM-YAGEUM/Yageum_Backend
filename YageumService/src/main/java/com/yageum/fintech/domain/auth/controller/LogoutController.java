package com.yageum.fintech.domain.auth.controller;

import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.domain.auth.service.AuthService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 로그아웃 API", description = "사용자 로그아웃 및 토큰 재발급과 관련된 API")
@RestController
@RequestMapping("/api/v1/logout")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LogoutController {

    private final AuthService authService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    // 토큰 재발급
    @Operation(summary = "토큰 재발급")
    @PatchMapping("/reissue")
    public CommonResult reissue(HttpServletRequest request,
                                HttpServletResponse response) {

        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        JWTAuthResponse newAccessToken = authService.reissueAccessToken(refreshToken);
        return responseService.getSingleResult(newAccessToken);
    }

}
