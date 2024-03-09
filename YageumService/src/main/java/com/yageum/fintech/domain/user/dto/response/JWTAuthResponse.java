package com.yageum.fintech.domain.user.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

}

