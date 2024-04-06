package com.yageum.fintech.domain.tenant.dto.response;

import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import lombok.*;

@Getter
@Builder
public class GetTenantResponseDto {
    private Long tenant_id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String message;

    public static GetTenantResponseDto from(Tenant tenant){
        return GetTenantResponseDto.builder()
                .tenant_id(tenant.getTenant_id())
                .username(tenant.getUsername())
                .email(tenant.getEmail())
                .name(tenant.getName())
                .phone(tenant.getPhone())
                .build();
    }

    //오류 응답
    public GetTenantResponseDto(String message) {
        this.message = message;
    }
}
