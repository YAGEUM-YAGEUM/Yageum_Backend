package com.yageum.fintech.domain.lessor.dto.response;

import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import com.yageum.fintech.domain.lessor.infrastructure.LessorProfile;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
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
public class GetLessorProfileDto {

    private Long profileId;

    /*lessor 정보 추가*/
    private String name;
    private String phone;
    private String email;

    private String title;
    private String introduce;
    private Gender gender;
    private String job;
    private Integer ownedProperty;
    private String location;

    public static GetLessorProfileDto from(Lessor lessor, LessorProfile lessorProfile){
        return GetLessorProfileDto.builder()
                .profileId(lessorProfile.getProfileId())
                .name(lessor.getName()) //추가
                .phone(lessor.getPhone()) //추가
                .email(lessor.getEmail()) //추가
                .title(lessorProfile.getTitle())
                .introduce(lessorProfile.getIntroduce())
                .gender(lessorProfile.getGender())
                .job(lessorProfile.getJob())
                .ownedProperty(lessorProfile.getOwnedProperty())
                .location(lessorProfile.getLocation())
                .build();
    }
}
