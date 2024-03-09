package com.yageum.fintech.domain.project.dto.response;

import com.yageum.fintech.domain.project.infrastructure.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectUserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;

    public static GetProjectUserResponseDto from(GetUserResponseDto getUserResponseDto, Role role){
        return GetProjectUserResponseDto.builder()
                .userId(getUserResponseDto.getId())
                .name(getUserResponseDto.getName())
                .email(getUserResponseDto.getEmail())
                .phoneNumber(getUserResponseDto.getPhoneNumber())
                .role(role.getDescription())
                .build();
    }
}
