package com.yageum.fintech.domain.auth.controller;

import com.yageum.fintech.domain.auth.dto.request.LoginRequest;
import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.auth.service.AuthService;
import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 인증 API", description = "사용자 로그인, 로그아웃과 관련된 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @Operation(summary = "임차인 로그인")
    @PostMapping("/tenant/login")
    public CommonResult tenantLogin(@RequestBody LoginRequest loginRequest){
        JWTAuthResponse token = authService.tenantLogin(loginRequest);
        return responseService.getSingleResult(token);
    }

    @Operation(summary = "임대인 로그인")
    @PostMapping("/lessor/login")
    public CommonResult lessorLogin(@RequestBody LoginRequest loginRequest){
        JWTAuthResponse token = authService.lessorLogin(loginRequest);
        return responseService.getSingleResult(token);
    }

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
