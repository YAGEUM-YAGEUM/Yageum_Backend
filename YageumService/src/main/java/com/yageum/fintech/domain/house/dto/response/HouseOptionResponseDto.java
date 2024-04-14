package com.yageum.fintech.domain.house.dto.response;

import com.yageum.fintech.domain.house.infrastructure.HouseOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseOptionResponseDto {

    private long houseId;
    private boolean bedroom;
    private boolean inductionCooktop;
    private boolean microwave;
    private boolean closet;
    private boolean refrigerator;
    private boolean airConditioner;
    private boolean washingMachine;
    private boolean dryer;
    private boolean television;
    private boolean cctv;

    public static HouseOptionResponseDto from(HouseOption houseOption) {
        return HouseOptionResponseDto.builder()
                        .houseId(houseOption.getHouse().getHouseId())
                        .bedroom(houseOption.isBedroom())
                        .inductionCooktop(houseOption.isInductionCooktop())
                        .microwave(houseOption.isMicrowave())
                        .closet(houseOption.isCloset())
                        .refrigerator(houseOption.isRefrigerator())
                        .airConditioner(houseOption.isAirConditioner())
                        .washingMachine(houseOption.isWashingMachine())
                        .dryer(houseOption.isDryer())
                .       television(houseOption.isTelevision())
                        .cctv(houseOption.isCctv())
                        .build();
    }
}
