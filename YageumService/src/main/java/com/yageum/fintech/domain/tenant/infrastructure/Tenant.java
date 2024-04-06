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
public class Tenant {

    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantId;

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

    //프로필 정보와 1:1 연관관계
    @OneToOne(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TenantProfile profile;

    private Tenant(Optional<Tenant> tenant) {
        if (tenant.isPresent()) {
            Tenant entity = tenant.get();
            this.tenantId = entity.getTenantId();
            this.username = entity.getUsername();
            this.email = entity.getEmail();
            this.name = entity.getName();
            this.phone = entity.getPhone();
            this.encryptedPwd = entity.getEncryptedPwd();
        }
    }

    public Tenant() {
    }

    public static Tenant of(Optional<Tenant> userEntity) {
        return new Tenant(userEntity);
    }

}
