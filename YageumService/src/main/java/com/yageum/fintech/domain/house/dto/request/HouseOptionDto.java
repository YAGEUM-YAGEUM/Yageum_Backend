package com.yageum.fintech.domain.house.dto.request;

import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.house.infrastructure.HouseOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HouseOptionDto {

    @NotNull(message = "침대 여부를 입력해주세요.")
    @Schema(description = "침대 여부", example = "false")
    private boolean bedroom;

    @NotNull(message = "인덕션 여부를 입력해주세요.")
    @Schema(description = "인덕션 여부", example = "false")
    private boolean inductionCooktop;

    @NotNull(message = "전자레인지 여부를 입력해주세요.")
    @Schema(description = "전자레인지 여부", example = "false")
    private boolean microwave;

    @NotNull(message = "옷장 여부를 입력해주세요.")
    @Schema(description = "옷장 여부", example = "false")
    private boolean closet;

    @NotNull(message = "냉장고 여부를 입력해주세요.")
    @Schema(description = "냉장고 여부", example = "true")
    private boolean refrigerator;

    @NotNull(message = "에어컨 여부를 입력해주세요.")
    @Schema(description = "에어컨 여부", example = "false")
    private boolean airConditioner;

    @NotNull(message = "세탁기 여부를 입력해주세요.")
    @Schema(description = "세탁기 여부", example = "false")
    private boolean washingMachine;

    @NotNull(message = "건조기 여부를 입력해주세요.")
    @Schema(description = "건조기 여부", example = "false")
    private boolean dryer;

    @NotNull(message = "TV 여부를 입력해주세요.")
    @Schema(description = "TV 여부", example = "false")
    private boolean television;

    @NotNull(message = "CCTV 여부를 입력해주세요.")
    @Schema(description = "CCTV 여부", example = "false")
    private boolean cctv;

    public HouseOption toEntity(House house){
        return HouseOption.builder()
                .house(house)
                .bedroom(bedroom)
                .inductionCooktop(inductionCooktop)
                .microwave(microwave)
                .closet(closet)
                .refrigerator(refrigerator)
                .airConditioner(airConditioner)
                .washingMachine(washingMachine)
                .dryer(dryer)
                .television(television)
                .cctv(cctv)
                .build();
    }
}
