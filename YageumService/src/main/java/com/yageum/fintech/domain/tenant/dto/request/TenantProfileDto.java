package com.yageum.fintech.domain.tenant.dto.request;

import com.yageum.fintech.domain.tenant.infrastructure.Gender;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TenantProfileDto {

    @NotBlank(message = "한줄소개를 입력해주세요.")
    @Size(min = 5, max = 30, message = "한줄소개는 5자에서 30자 사이여야 합니다.")
    @Schema(description = "한줄소개", example = "깔끔한 성격을 가진 임차인")
    private String title;

    @NotBlank(message = "소개를 입력해주세요.")
    @Size(max = 255, message = "소개는 250자 이내로 작성해야 합니다.")
    @Schema(description = "소개", example = "청소를 좋아하고 주변을 깨끗하게 유지하는 임차인입니다. 2년 이상 거주하고 싶습니다.")
    private String introduce;

    @NotNull(message = "성별을 선택해주세요.")
    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @NotBlank(message = "직업을 입력해주세요.")
    @Schema(description = "직업", example = "개발자")
    private String job;

    @NotNull(message = "자취횟수를 입력해주세요.")
    @Schema(description = "자취횟수", example = "3")
    private Integer experience;

    @NotNull(message = "나이를 입력해주세요.")
    @Schema(description = "나이", example = "25")
    private Integer age;

    @NotBlank(message = "선호지역구를 입력해주세요.")
    @Schema(description = "선호지역구", example = "강남구")
    private String preferredLocation;

    @NotBlank(message = "선호 방타입을 입력해주세요.")
    @Schema(description = "선호 방타입", example = "원룸")
    private String preferredType;

    public TenantProfile toEntity(Tenant tenant){
        return TenantProfile.builder()
                .tenant(tenant)
                .title(title)
                .introduce(introduce)
                .gender(gender)
                .job(job)
                .experience(experience)
                .age(age)
                .preferredLocation(preferredLocation)
                .preferredType(preferredType)
                .build();
    }
}
