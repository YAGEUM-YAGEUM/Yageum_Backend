package com.yageum.fintech.domain.tenant.dto.response;

import com.yageum.fintech.domain.tenant.infrastructure.Gender;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTenantProfileDto {

    private Long profileId;

    /*tenant 정보 추가*/
    private String name;
    private String phone;
    private String email;

    private String title;
    private String introduce;
    private Gender gender;
    private String job;
    private Integer experience;
    private Integer age;
    private String preferredLocation;
    private String preferredType;

    public static GetTenantProfileDto from(Tenant tenant, TenantProfile tenantProfile){
        return GetTenantProfileDto.builder()
                .profileId(tenantProfile.getProfileId())
                .name(tenant.getName()) //추가
                .phone(tenant.getPhone()) //추가
                .email(tenant.getEmail()) //추가
                .title(tenantProfile.getTitle())
                .introduce(tenantProfile.getIntroduce())
                .gender(tenantProfile.getGender())
                .job(tenantProfile.getJob())
                .experience(tenantProfile.getExperience())
                .age(tenantProfile.getAge())
                .preferredLocation(tenantProfile.getPreferredLocation())
                .preferredType(tenantProfile.getPreferredType())
                .build();
    }
}
