package com.yageum.fintech.domain.user.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id") // 외래 키 필드 추가
    private Long product_id;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name= "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "encryptedPwd", nullable = false, length = 100)
    private String encryptedPwd;

    @Column(name = "isApproved", nullable = false)
    private boolean isApproved;

    private UserEntity(Optional<UserEntity> userEntity) {
        this.id = id;
        this.product_id = product_id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.encryptedPwd = encryptedPwd;
        this.isApproved = isApproved;
    }

    public UserEntity() {
    }

    public static UserEntity of(Optional<UserEntity> userEntity) {
        return new UserEntity(userEntity);
    }

}
