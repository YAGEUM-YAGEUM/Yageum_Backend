package com.yageum.fintech.domain.auth.service;

import com.yageum.fintech.domain.auth.dto.request.LoginRequest;
import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;

import java.util.Optional;

public interface AuthService {

    Optional<Tenant> findOne(String username);
    JWTAuthResponse tenantLogin(LoginRequest loginRequest);
    JWTAuthResponse lessorLogin(LoginRequest loginRequest);
    JWTAuthResponse reissueAccessToken(String encryptedRefreshToken);

}
