package com.yageum.fintech.domain.lessor.infrastructure;

import com.yageum.fintech.domain.lessor.dto.request.LessorProfileDto;
import com.yageum.fintech.domain.tenant.infrastructure.Gender;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "lessor_profile")
public class LessorProfile {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessor_id")
    private Lessor lessor;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "introduce", nullable = false, length = 255)
    private String introduce;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "ownedProperty", nullable = false)
    private Integer ownedProperty;

    @Column(name = "location", nullable = false)
    private String location;

    @Builder
    public LessorProfile(Long profileId, Lessor lessor, String title, String introduce, Gender gender, String job, Integer ownedProperty, String location) {
        this.profileId = profileId;
        this.lessor = lessor;
        this.title = title;
        this.introduce = introduce;
        this.gender = gender;
        this.job = job;
        this.ownedProperty = ownedProperty;
        this.location = location;
    }

    public void update(LessorProfileDto lessorProfileDto) {
        this.title = lessorProfileDto.getTitle();
        this.introduce = lessorProfileDto.getIntroduce();
        this.gender = lessorProfileDto.getGender();
        this.job = lessorProfileDto.getJob();
        this.ownedProperty = lessorProfileDto.getOwnedProperty();
        this.location = lessorProfileDto.getLocation();
    }
}
