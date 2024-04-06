package com.yageum.fintech.domain.tenant.controller;

import com.yageum.fintech.domain.tenant.dto.request.LoginRequest;
import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.tenant.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.service.TenantService;
import com.yageum.fintech.global.config.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Exception.BusinessLogicException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "임차인 API", description = "회원가입, 프로필 등록 및 수정, 조회, 이메일 인증과 관련된 API")
@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TenantController {

    private final TenantService tenantService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;

    //health-check
    @Operation(summary = "사용자 서비스 상태 확인")
    @GetMapping("/health_check")
    public String status(){
        return "It's Working in Tenant Service";
    }

    // 로그인
    @Operation(summary = "사용자 로그인")
    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginRequest loginRequest){
        JWTAuthResponse token = tenantService.login(loginRequest);
        return responseService.getSingleResult(token);
    }

    // 회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/")
    public CommonResult register(@RequestBody TenantRequestDto tenantRequestDto){
        return tenantService.register(tenantRequestDto);
    }

    // 임차인 정보 - 프로필 등록
    @Operation(summary = "임차인 프로필 등록")
    @PostMapping("/profile/{tenantId}")
    public CommonResult createTenantProfile(@PathVariable Long tenantId, @RequestBody TenantProfileDto tenantRequestDto){
        tenantService.createTenantProfile(tenantId, tenantRequestDto);
        return responseService.getSuccessfulResult();
    }
    

    // 토큰 재발급
    @Operation(summary = "토큰 재발급")
    @PatchMapping("/reissue")
    public CommonResult reissue(HttpServletRequest request,
                                                   HttpServletResponse response) {

        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        JWTAuthResponse newAccessToken = tenantService.reissueAccessToken(refreshToken);
        return responseService.getSingleResult(newAccessToken);
    }

    // 이메일 인증번호 전송
    @Operation(summary = "이메일 인증번호 전송")
    @PostMapping("/emails/verification-requests")
    public CommonResult sendMessage(@RequestParam("email") @Valid String email) {
        tenantService.sendCodeToEmail(email);
        return responseService.getSuccessfulResult();
    }

    //이메일 인증번호 검증
    @Operation(summary = "이메일 인증번호 검증")
    @GetMapping("/emails/verifications")
    public EmailVerificationResult verificationEmail(@RequestParam("email") @Valid @Email String email,
                                            @RequestParam("code") String authCode) {
        return tenantService.verifiedCode(email, authCode);
    }

    // 사용자 정보 조회 by userId
    @Operation(summary = "userId로 사용자 정보 조회")
    @GetMapping("/response_user/{userId}")
    public CommonResult findUserResponseByUserId(@PathVariable("userId") Long userId) {
        try {
            GetTenantResponseDto tenantResponse = tenantService.getUserResponseByUserId(userId);
            return responseService.getSingleResult(tenantResponse);
        } catch (BusinessLogicException e) {
            ExceptionList exceptionList = e.getExceptionList();
            return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
        }
    }

    // 사용자 정보 조회 by email
    @Operation(summary = "이메일로 사용자 정보 조회")
    @GetMapping("/response_userByEmail/{email}")
    public CommonResult findUserResponseByEmail(@PathVariable String email) {
        try {
            GetTenantResponseDto tenantResponse = tenantService.getUserResponseByEmail(email);
            return responseService.getSingleResult(tenantResponse);
        } catch (BusinessLogicException e) {
            ExceptionList exceptionList = e.getExceptionList();
            return responseService.getFailResult(exceptionList.getCode(), exceptionList.getMessage());
        }
    }

}

