package com.yageum.fintech.domain.auth.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
public class JWTAuthResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

    @Builder
    public JWTAuthResponse(String tokenType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }
}

