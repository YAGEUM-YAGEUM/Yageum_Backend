package com.yageum.fintech.domain.project.dto.response;

import com.yageum.fintech.domain.project.infrastructure.Role;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
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

    public static GetProjectUserResponseDto from(GetTenantResponseDto getTenantResponseDto, Role role){
        return GetProjectUserResponseDto.builder()
                .userId(getTenantResponseDto.getId())
                .name(getTenantResponseDto.getName())
                .email(getTenantResponseDto.getEmail())
                .phoneNumber(getTenantResponseDto.getPhoneNumber())
                .role(role.getDescription())
                .build();
    }
}
