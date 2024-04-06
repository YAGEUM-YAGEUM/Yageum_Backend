package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.project.dto.response.GetProjectResponseDto;
import com.yageum.fintech.domain.tenant.dto.request.LoginRequest;
import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.tenant.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.global.model.Result.CommonResult;

import java.util.Optional;

public interface TenantService {

    Optional<Tenant> findOne(String email);

    JWTAuthResponse login(LoginRequest loginRequest);

    CommonResult register(TenantRequestDto tenantRequestDto);

    void createTenantProfile(Long tenantId, TenantProfileDto tenantProfileDto);

    GetTenantProfileDto getTenantProfile(Long tenantId);

    String getUsername(Long userId);

    GetTenantResponseDto getUserResponseByUserId(Long userId);

    GetTenantResponseDto getUserResponseByEmail(String email);

    JWTAuthResponse reissueAccessToken(String encryptedRefreshToken);

    void sendCodeToEmail(String toEmail);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
