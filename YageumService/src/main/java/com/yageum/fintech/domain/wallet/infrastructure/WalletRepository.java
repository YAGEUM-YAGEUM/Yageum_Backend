package com.yageum.fintech.domain.wallet.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByUsername(String username);

    Boolean existsByUsername(String username);

}
