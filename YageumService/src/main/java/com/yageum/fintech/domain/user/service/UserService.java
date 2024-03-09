package com.yageum.fintech.domain.user.service;

import com.yageum.fintech.domain.user.dto.request.LoginRequest;
import com.yageum.fintech.domain.user.dto.request.CreateUserRequestDto;
import com.yageum.fintech.domain.user.dto.response.GetUserResponseDto;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.domain.user.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.user.infrastructure.UserEntity;

import java.util.Optional;

public interface UserService{

    Optional<UserEntity> findOne(String email);

    JWTAuthResponse login(LoginRequest loginRequest);

    String register(CreateUserRequestDto createUserRequestDto);

    GetUserResponseDto getUserResponseByUserId(Long userId);

    GetUserResponseDto findUserResponseByEmail(String email);

    JWTAuthResponse reissueAccessToken(String encryptedRefreshToken);

    void sendCodeToEmail(String toEmail);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
