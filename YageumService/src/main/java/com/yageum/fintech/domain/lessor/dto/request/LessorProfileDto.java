package com.yageum.fintech.domain.lessor.dto.request;

import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import com.yageum.fintech.domain.lessor.infrastructure.LessorProfile;
import com.yageum.fintech.domain.tenant.infrastructure.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LessorProfileDto {

    @NotBlank(message = "한줄소개를 입력해주세요.")
    @Size(min = 5, max = 30, message = "한줄소개는 5자에서 30자 사이여야 합니다.")
    @Schema(description = "한줄소개", example = "가지고 있는 매물이 많은 임대인")
    private String title;

    @NotBlank(message = "소개를 입력해주세요.")
    @Size(max = 255, message = "소개는 250자 이내로 작성해야 합니다.")
    @Schema(description = "소개", example = "임대인들과 소통이 잘 되는 임차인입니다.")
    private String introduce;

    @NotNull(message = "성별을 선택해주세요.")
    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @NotBlank(message = "직업을 입력해주세요.")
    @Schema(description = "직업", example = "회사원")
    private String job;

    @NotNull(message = "소유한 부동산 수를 입력해주세요.")
    @Schema(description = "소유한 부동산 수", example = "10")
    private Integer ownedProperty;

    @NotBlank(message = "선호지역을 입력해주세요.")
    @Schema(description = "선호지역", example = "서울 강남구")
    private String location;

    public LessorProfile toEntity(Lessor lessor) {
        return LessorProfile.builder()
                .lessor(lessor)
                .title(title)
                .introduce(introduce)
                .gender(gender)
                .job(job)
                .ownedProperty(ownedProperty)
                .location(location)
                .build();
    }
}
