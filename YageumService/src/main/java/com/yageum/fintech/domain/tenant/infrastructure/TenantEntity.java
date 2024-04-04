package com.yageum.fintech.domain.tenant.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "tenant")
@AllArgsConstructor
public class TenantEntity {

    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenant_id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "encryptedPwd", nullable = false, length = 100)
    private String encryptedPwd;

    @Column(name= "name", nullable = false, length = 10)
    private String name;

    @Column(name= "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;



    private TenantEntity(Optional<TenantEntity> userEntity) {
        if (userEntity.isPresent()) {
            TenantEntity entity = userEntity.get();
            this.tenant_id = entity.getTenant_id();
            this.username = entity.getUsername();
            this.email = entity.getEmail();
            this.name = entity.getName();
            this.phone = entity.getPhone();
            this.encryptedPwd = entity.getEncryptedPwd();
        }
    }

    public TenantEntity() {
    }

    public static TenantEntity of(Optional<TenantEntity> userEntity) {
        return new TenantEntity(userEntity);
    }

}
