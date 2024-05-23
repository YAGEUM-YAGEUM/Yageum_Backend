package com.yageum.fintech.domain.lessor.infrastructure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "lessor")
@AllArgsConstructor
public class Lessor {

    @Id
    @Column(name = "lessor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessorId;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username; //아이디

    @Column(name = "encryptedPwd", nullable = false, length = 100)
    private String encryptedPwd;

    @Column(name= "name", nullable = false, length = 10)
    private String name;

    @Column(name= "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    //프로필 정보와 1:1 연관관계
    @OneToOne(mappedBy = "lessor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LessorProfile profile;

    private Lessor(Optional<Lessor> lessor) {
        if (lessor.isPresent()) {
            Lessor entity = lessor.get();
            this.lessorId = entity.getLessorId();
            this.username = entity.getUsername();
            this.email = entity.getEmail();
            this.name = entity.getName();
            this.phone = entity.getPhone();
            this.encryptedPwd = entity.getEncryptedPwd();
        }
    }

    public Lessor() {
    }

    public static Lessor of(Optional<Lessor> lessorEntity) {
        return new Lessor(lessorEntity);
    }

}
