package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.tenant.dto.request.LoginRequest;
import com.yageum.fintech.domain.tenant.dto.request.CreateTenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetUserResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.tenant.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.infrastructure.UserEntity;
import com.yageum.fintech.global.model.Result.CommonResult;

import java.util.Optional;

public interface TenantService {

    Optional<UserEntity> findOne(String email);

    JWTAuthResponse login(LoginRequest loginRequest);

    CommonResult register(CreateTenantRequestDto createTenantRequestDto);

    String getUsername(Long userId);

    GetUserResponseDto getUserResponseByUserId(Long userId);

    GetUserResponseDto getUserResponseByEmail(String email);

    JWTAuthResponse reissueAccessToken(String encryptedRefreshToken);

    void sendCodeToEmail(String toEmail);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
