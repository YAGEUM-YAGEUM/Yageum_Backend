package com.yageum.fintech.domain.user.dto.response;

import com.yageum.fintech.domain.user.infrastructure.UserEntity;
import lombok.*;

@Getter
@Builder
public class GetUserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private boolean isApproved;
    private String message;

    public static GetUserResponseDto from(UserEntity userEntity){
        return GetUserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .phoneNumber(userEntity.getPhoneNumber())
                .isApproved(userEntity.isApproved())
                .build();
    }

    //오류 응답
    public GetUserResponseDto(String message) {
        this.message = message;
    }
}
