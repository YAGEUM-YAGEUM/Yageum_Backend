package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.global.model.Result.CommonResult;


public interface TenantService {

    CommonResult register(TenantRequestDto tenantRequestDto);

    void createTenantProfile(Long tenantId, TenantProfileDto tenantProfileDto);

    void updateTenantProfile(Long profileId, TenantProfileDto tenantProfileDto);

    GetTenantProfileDto getTenantProfile(Long tenantId);

    String getUsername(Long userId);

    GetTenantResponseDto getUserResponseByUserId(Long userId);

    GetTenantResponseDto getUserResponseByEmail(String email);

    void sendCodeToEmail(String toEmail);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
