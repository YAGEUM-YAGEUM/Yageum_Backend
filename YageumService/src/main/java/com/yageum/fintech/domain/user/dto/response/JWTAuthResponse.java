package com.yageum.fintech.domain.user.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
public class JWTAuthResponse {

    @Builder
    public JWTAuthResponse(String tokenType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

}

