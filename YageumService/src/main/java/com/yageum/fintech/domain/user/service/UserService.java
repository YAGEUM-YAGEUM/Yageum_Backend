package com.yageum.fintech.domain.user.service;

import com.yageum.fintech.domain.user.dto.request.RequestLogin;
import com.yageum.fintech.domain.user.dto.request.RequestUser;
import com.yageum.fintech.domain.user.dto.response.EmailVerificationResult;
import com.yageum.fintech.domain.user.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.user.dto.response.UserResponse;
import com.yageum.fintech.domain.user.infrastructure.UserEntity;

import java.util.Optional;

public interface UserService{

    Optional<UserEntity> findOne(String email);

    JWTAuthResponse login(RequestLogin requestLogin);

    String register(RequestUser requestUser);

    UserResponse getUserResponseByUserId(Long userId);

    UserResponse findUserResponseByEmail(String email);

    JWTAuthResponse reissueAccessToken(String encryptedRefreshToken);

    void sendCodeToEmail(String toEmail);

    EmailVerificationResult verifiedCode(String email, String authCode);
}
