package com.yageum.fintech.domain.tenant.dto.response;

import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import lombok.*;

@Getter
public class GetTenantResponseDto {
    private Long tenantId;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String message;

    public static GetTenantResponseDto from(Tenant tenant){
        return new GetTenantResponseDto(
                tenant.getTenantId(),
                tenant.getUsername(),
                tenant.getEmail(),
                tenant.getName(),
                tenant.getPhone()
        );
    }

    //오류 응답
    public GetTenantResponseDto(String message) {
        this.message = message;
    }

    // 필드를 가지고 있는 생성자
    public GetTenantResponseDto(Long tenantId, String username, String email, String name, String phone) {
        this.tenantId = tenantId;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
