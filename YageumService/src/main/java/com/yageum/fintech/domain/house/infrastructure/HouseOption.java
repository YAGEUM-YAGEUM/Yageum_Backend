package com.yageum.fintech.domain.house.infrastructure;


import com.yageum.fintech.domain.house.dto.request.HouseOptionDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "house_option")
public class HouseOption {

    @Id
    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "bedroom", nullable = false)
    private boolean bedroom;

    @Column(name = "induction_cooktop", nullable = false)
    private boolean inductionCooktop;

    @Column(name = "microwave", nullable = false)
    private boolean microwave;

    @Column(name = "closet", nullable = false)
    private boolean closet;

    @Column(name = "refrigerator", nullable = false)
    private boolean refrigerator;

    @Column(name = "air_conditioner", nullable = false)
    private boolean airConditioner;

    @Column(name = "washing_machine", nullable = false)
    private boolean washingMachine;

    @Column(name = "dryer", nullable = false)
    private boolean dryer;

    @Column(name = "television", nullable = false)
    private boolean television;

    @Column(name = "cctv", nullable = false)
    private boolean cctv;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "house_id")
    private House house;

    @Builder
    public HouseOption(Long houseId, House house, boolean bedroom, boolean inductionCooktop, boolean microwave,
                       boolean closet, boolean refrigerator, boolean airConditioner, boolean washingMachine,
                       boolean dryer, boolean television, boolean cctv) {
        this.houseId = houseId;
        this.house = house; // house 필드 초기화
        this.bedroom = bedroom;
        this.inductionCooktop = inductionCooktop;
        this.microwave = microwave;
        this.closet = closet;
        this.refrigerator = refrigerator;
        this.airConditioner = airConditioner;
        this.washingMachine = washingMachine;
        this.dryer = dryer;
        this.television = television;
        this.cctv = cctv;
    }

    public void update(HouseOptionDto houseOptionDto) {
        this.bedroom = bedroom;
        this.inductionCooktop = inductionCooktop;
        this.microwave = microwave;
        this.closet = closet;
        this.refrigerator = refrigerator;
        this.airConditioner = airConditioner;
        this.washingMachine = washingMachine;
        this.dryer = dryer;
        this.television = television;
        this.cctv = cctv;    }
}
