package com.yageum.fintech.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private boolean isApproved;
}
