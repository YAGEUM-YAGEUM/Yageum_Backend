package com.yageum.fintech.domain.wallet.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @NotNull
    @Column(name = "username", length = 16, nullable = false)
    private String usearname;

    @NotNull
    @Column(name = "accountAddress", nullable = false)
    private String accountAddress;

}
