package com.yageum.fintech.domain.tenant.dto.response;

import com.yageum.fintech.domain.tenant.infrastructure.TenantEntity;
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

    public static GetTenantResponseDto from(TenantEntity tenantEntity){
        return GetTenantResponseDto.builder()
                .tenant_id(tenantEntity.getTenant_id())
                .username(tenantEntity.getUsername())
                .email(tenantEntity.getEmail())
                .name(tenantEntity.getName())
                .phone(tenantEntity.getPhone())
                .build();
    }

    //오류 응답
    public GetTenantResponseDto(String message) {
        this.message = message;
    }
}
