package com.yageum.fintech.domain.tenant.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "tenant_profile")
@AllArgsConstructor
public class TenantProfileEntity {

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profile_id;

    @Column(name = "tenant_id") // 외래 키 필드 추가
    private Long tenant_id;

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


    public TenantProfileEntity(Optional<TenantProfileEntity> tenantProfileEntity) {
        if (tenantProfileEntity.isPresent()) {
            TenantProfileEntity entity = tenantProfileEntity.get();
            this.profile_id = entity.getProfile_id();
            this.tenant_id = entity.getTenant_id();
            this.title = entity.getTitle();
            this.introduce = entity.getIntroduce();
            this.gender = entity.getGender();
            this.job = entity.getJob();
            this.experience = entity.getExperience();
            this.age = entity.getAge();
            this.preferredLocation = entity.getPreferredLocation();
            this.preferredType = entity.getPreferredType();
        }
    }

    public TenantProfileEntity of(Optional<TenantProfileEntity> tenantProfileEntity) {
        return new TenantProfileEntity(tenantProfileEntity);
    }

    public TenantProfileEntity() {
    }
}
