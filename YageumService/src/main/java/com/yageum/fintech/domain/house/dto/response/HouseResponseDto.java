package com.yageum.fintech.domain.house.dto.response;

import com.yageum.fintech.domain.house.infrastructure.Category;
import com.yageum.fintech.domain.house.infrastructure.Direction;
import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseResponseDto {
    private Long houseId;
    private Lessor lessor;
    private Category category;
    private Long deposit;
    private Long monthlyRent;
    private Long fee;
    private Integer activeFloor;
    private Integer totalFloor;
    private Float exclusiveArea;
    private Integer totalRoom;
    private Integer totalBathroom;
    private Direction direction;
    private String heatingType;
    private Integer parking;
    private Date moveInDate;
    private String buildingPurpose;
    private Date approvedDate;
    private String description;
    private String location;

    public static HouseResponseDto from(Lessor lessor, House house){
        return HouseResponseDto.builder()
                .houseId(house.getHouseId())
                .lessor(lessor)
                .category(house.getCategory())
                .deposit(house.getDeposit())
                .monthlyRent(house.getMonthlyRent())
                .fee(house.getFee())
                .activeFloor(house.getActiveFloor())
                .totalFloor(house.getTotalFloor())
                .exclusiveArea(house.getExclusiveArea())
                .totalRoom(house.getTotalRoom())
                .totalBathroom(house.getTotalBathroom())
                .direction(house.getDirection())
                .heatingType(house.getHeatingType())
                .parking(house.getParking())
                .moveInDate(house.getMoveInDate())
                .buildingPurpose(house.getBuildingPurpose())
                .approvedDate(house.getApprovedDate())
                .description(house.getDescription())
                .location(house.getLocation())
                .build();
    }
}
