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
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 로그아웃 API", description = "사용자 로그아웃 및 토큰 재발급과 관련된 API")
@RestController
@RequestMapping("/api/v1/logout")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LogoutController {

    private final AuthService authService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그아웃", description = "사용자(임대인, 임차인 공통)의 로그아웃과 관련된 API")
    @DeleteMapping
    public CommonResult logout(HttpServletRequest request) {

        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        authService.logout(accessToken, refreshToken);

        return responseService.getSuccessfulResult();
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급", description = "리프레쉬 토큰으로 액세스 토큰을 재발급 하는 API")
    @PatchMapping("/reissue")
    public CommonResult reissue(HttpServletRequest request,
                                HttpServletResponse response) {

        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        JWTAuthResponse newAccessToken = authService.reissueAccessToken(refreshToken);
        return responseService.getSingleResult(newAccessToken);
    }

}
