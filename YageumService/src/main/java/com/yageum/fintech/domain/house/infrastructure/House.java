package com.yageum.fintech.domain.house.infrastructure;

import com.yageum.fintech.domain.house.dto.request.HouseRequestDto;
import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "house")
public class House {

    @Id
    @Column(name="house_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessor_id", nullable = false)
    private Lessor lessor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Long deposit;

    @Column(nullable = false)
    private Long monthlyRent;

    @Column(nullable = false)
    private Long fee;

    @Column(nullable = false)
    private Integer activeFloor;

    @Column(nullable = false)
    private Integer totalFloor;

    @Column(nullable = false)
    private Float exclusiveArea;

    @Column(nullable = false)
    private Integer totalRoom;

    @Column(nullable = false)
    private Integer totalBathroom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private String heatingType;

    @Column(nullable = false)
    private Integer parking;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date moveInDate;

    @Column
    private String buildingPurpose;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date approvedDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Builder
    public House(Long houseId, Lessor lessor, Category category, Long deposit, Long monthlyRent, Long fee,
                 Integer activeFloor, Integer totalFloor, Float exclusiveArea, Integer totalRoom,
                 Integer totalBathroom, Direction direction, String heatingType, Integer parking,
                 Date moveInDate, String buildingPurpose, Date approvedDate, String description,
                 String location) {
        this.houseId = houseId;
        this.lessor = lessor;
        this.category = category;
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
        this.fee = fee;
        this.activeFloor = activeFloor;
        this.totalFloor = totalFloor;
        this.exclusiveArea = exclusiveArea;
        this.totalRoom = totalRoom;
        this.totalBathroom = totalBathroom;
        this.direction = direction;
        this.heatingType = heatingType;
        this.parking = parking;
        this.moveInDate = moveInDate;
        this.buildingPurpose = buildingPurpose;
        this.approvedDate = approvedDate;
        this.description = description;
        this.location = location;
    }

    public void update(HouseRequestDto houseRequestDto) {
        this.category = houseRequestDto.getCategory();
        this.deposit = houseRequestDto.getDeposit();
        this.monthlyRent = houseRequestDto.getMonthlyRent();
        this.fee = houseRequestDto.getFee();
        this.activeFloor = houseRequestDto.getActiveFloor();
        this.totalFloor = houseRequestDto.getTotalFloor();
        this.exclusiveArea = houseRequestDto.getExclusiveArea();
        this.totalRoom = houseRequestDto.getTotalRoom();
        this.totalBathroom = houseRequestDto.getTotalBathroom();
        this.direction = houseRequestDto.getDirection();
        this.heatingType = houseRequestDto.getHeatingType();
        this.parking = houseRequestDto.getParking();
        this.moveInDate = houseRequestDto.getMoveInDate();
        this.buildingPurpose = houseRequestDto.getBuildingPurpose();
        this.approvedDate = houseRequestDto.getApprovedDate();
        this.description = houseRequestDto.getDescription();
        this.location = houseRequestDto.getLocation();
    }

}
