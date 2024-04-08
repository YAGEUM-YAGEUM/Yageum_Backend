package com.yageum.fintech.domain.tenant.infrastructure;

import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tenant_profile")
public class TenantProfile {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "introduce", nullable = false, length = 255)
    private String introduce;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "preferred_location", nullable = false)
    private String preferredLocation;

    @Column(name = "preferred_type", nullable = false)
    private String preferredType;

    @Builder
    public TenantProfile(Long profileId, Tenant tenant, String title, String introduce, Gender gender, String job, Integer experience, Integer age, String preferredLocation, String preferredType) {
        this.profileId = profileId;
        this.tenant = tenant;
        this.title = title;
        this.introduce = introduce;
        this.gender = gender;
        this.job = job;
        this.experience = experience;
        this.age = age;
        this.preferredLocation = preferredLocation;
        this.preferredType = preferredType;
    }

    public void update(TenantProfileDto tenantProfileDto) {
        this.title = tenantProfileDto.getTitle();
        this.introduce = tenantProfileDto.getIntroduce();
        this.gender = tenantProfileDto.getGender();
        this.job = tenantProfileDto.getJob();
        this.age = tenantProfileDto.getAge();
        this.preferredLocation = tenantProfileDto.getPreferredLocation();
        this.preferredType = tenantProfileDto.getPreferredType();
    }

}
