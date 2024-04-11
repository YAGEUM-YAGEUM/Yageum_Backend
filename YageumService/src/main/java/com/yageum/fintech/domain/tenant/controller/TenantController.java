package com.yageum.fintech.domain.tenant.controller;

import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.tenant.service.TenantService;
import com.yageum.fintech.global.model.Exception.BusinessLogicException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "임차인 API", description = "임차인 회원가입, 프로필 등록 및 수정, 조회와 관련된 API")
@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TenantController {

    private final TenantService tenantService;
    private final ResponseService responseService;

    //health-check
    @Operation(summary = "사용자 서비스 상태 확인")
    @GetMapping()
    public String status(){
        return "It's Working in Tenant Service";
    }

    // 회원가입
    @Operation(summary = "임차인 회원가입")
    @PostMapping("/")
    public CommonResult register(@RequestBody TenantRequestDto tenantRequestDto){
        return tenantService.register(tenantRequestDto);
    }

    // 임차인 정보 - 프로필 등록
    @Operation(summary = "임차인 프로필 등록", description = "특정 임차인(사용자)의 프로필 정보를 등록하는 API")
    @PostMapping("/profile/{tenantId}")
    public CommonResult createTenantProfile(@PathVariable Long tenantId, @RequestBody TenantProfileDto tenantRequestDto){
        tenantService.createTenantProfile(tenantId, tenantRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 임차인 정보 - 프로필 조회
    @Operation(summary = "임차인 프로필 조회", description = "특정 임차인(사용자)의 프로필 정보를 조회하는 API")
    @GetMapping("/profile/{tenantId}")
    public CommonResult getTenantProfile(@PathVariable Long tenantId){
        GetTenantProfileDto profile = tenantService.getTenantProfile(tenantId);
        return responseService.getSingleResult(profile);
    }

    // 임차인 정보 - 프로필 수정
    @Operation(summary = "임차인 프로필 수정", description = "프로필 ID로 프로필 정보를 수정하는 API")
    @PutMapping("/profile/{profileId}")
    public CommonResult updateTenantProfile(@PathVariable Long profileId, @RequestBody TenantProfileDto tenantProfileDto){
        tenantService.updateTenantProfile(profileId, tenantRequestDto);
        return responseService.getSuccessfulResult();
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

