package com.yageum.fintech.domain.house.dto.request;

import com.yageum.fintech.domain.house.infrastructure.Category;
import com.yageum.fintech.domain.house.infrastructure.Direction;
import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HouseRequestDto {

    @NotNull(message = "카테고리를 선택해주세요.")
    @Schema(description = "전세 또는 월세", example = "YEAR")
    private Category category;

    @NotNull(message = "보증금을 입력해주세요.")
    @Schema(description = "보증금", example = "1000000")
    private Long deposit;

    @NotNull(message = "월세를 입력해주세요.")
    @Schema(description = "월세", example = "100000")
    private Long monthlyRent;

    @NotNull(message = "관리비을 입력해주세요.")
    @Schema(description = "관리비", example = "50000")
    private Long fee;

    @NotNull(message = "해당 층수를 입력해주세요.")
    @Schema(description = "해당 층수", example = "2")
    private Integer activeFloor;

    @NotNull(message = "전체 층수를 입력해주세요.")
    @Schema(description = "전체 층수", example = "5")
    private Integer totalFloor;

    @NotNull(message = "전용 면적을 입력해주세요.")
    @Schema(description = "전용 면적", example = "50.5")
    private Float exclusiveArea;

    @NotNull(message = "방 개수를 입력해주세요.")
    @Schema(description = "방 개수", example = "3")
    private Integer totalRoom;

    @NotNull(message = "욕실 개수를 입력해주세요.")
    @Schema(description = "욕실 개수", example = "2")
    private Integer totalBathroom;

    @NotNull(message = "방향을 입력해주세요.")
    @Schema(description = "방향", example = "EAST")
    private Direction direction;

    @NotBlank(message = "난방 종류를 입력해주세요.")
    @Schema(description = "난방 종류", example = "개별난방")
    private String heatingType;

    @NotNull(message = "주차 가능 대수를 입력해주세요.")
    @Schema(description = "주차 가능 대수", example = "1")
    private Integer parking;

    @NotNull(message = "입주 가능일을 입력해주세요.")
    @Schema(description = "입주 가능일", example = "2024-04-30")
    private Date moveInDate;

    @Schema(description = "건물 용도", example = "주택")
    private String buildingPurpose;

    @NotNull(message = "건물 승인일을 입력해주세요.")
    @Schema(description = "건물 승인일", example = "2024-04-01")
    private Date approvedDate;

    @NotBlank(message = "설명을 입력해주세요.")
    @Size(max = 255, message = "설명은 255자 이내로 작성해야 합니다.")
    @Schema(description = "설명", example = "깔끔한 원룸입니다.")
    private String description;

    @NotBlank(message = "위치를 입력해주세요.")
    @Size(max = 255, message = "위치는 255자 이내로 작성해야 합니다.")
    @Schema(description = "위치", example = "서울시 강남구")
    private String location;

    public House toEntity(Lessor lessor){
        return House.builder()
                .lessor(lessor)
                .category(category)
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .fee(fee)
                .activeFloor(activeFloor)
                .totalFloor(totalFloor)
                .exclusiveArea(exclusiveArea)
                .totalRoom(totalRoom)
                .totalBathroom(totalBathroom)
                .direction(direction)
                .heatingType(heatingType)
                .parking(parking)
                .moveInDate(moveInDate)
                .buildingPurpose(buildingPurpose)
                .approvedDate(approvedDate)
                .description(description)
                .location(location)
                .build();
    }
}
