package com.yageum.fintech.domain.contract.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractDetailsRepository extends JpaRepository<ContractDetails, String> {

    Optional<ContractDetails> findByHash(String hash);
}
