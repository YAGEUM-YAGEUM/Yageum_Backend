package com.yageum.fintech.domain.auth.controller;

import com.yageum.fintech.domain.auth.dto.request.LoginRequest;
import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.auth.service.AuthService;
import com.yageum.fintech.domain.lessor.dto.request.LessorRequestDto;
import com.yageum.fintech.domain.lessor.service.LessorService;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.service.TenantService;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 인증 API", description = "사용자 로그인, 회원가입과 관련된 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final LessorService lessorService;
    private final TenantService tenantService;
    private final AuthService authService;
    private final ResponseService responseService;

    //인터셉터가 적용되지 않음
    //회원가입
    @Operation(summary = "임차인 회원가입")
    @PostMapping("/tenant/register")
    public CommonResult tenantRegister(@RequestBody TenantRequestDto tenantRequestDto){
        return tenantService.register(tenantRequestDto);
    }

    @Operation(summary = "임대인 회원가입")
    @PostMapping("/lessor/register")
    public CommonResult lessorRegister(@RequestBody LessorRequestDto lessorRequestDto){
        return lessorService.register(lessorRequestDto);
    }

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

    // 이메일 인증번호 전송
    @Operation(summary = "이메일 인증번호 전송")
    @PostMapping("/emails/verification-requests")
    public CommonResult sendMessage(@RequestParam("email") @Valid String email) {
        authService.sendCodeToEmail(email);
        return responseService.getSuccessfulResult();
    }

    //이메일 인증번호 검증
    @Operation(summary = "이메일 인증번호 검증")
    @GetMapping("/emails/verifications")
    public EmailVerificationResult verificationEmail(@RequestParam("email") @Valid @Email String email,
                                                     @RequestParam("code") String authCode) {
        return authService.verifiedCode(email, authCode);
    }

}
