package com.yageum.fintech.domain.tenant.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantProfileRepository extends JpaRepository<TenantProfileEntity, Long> {

    Optional<TenantProfileEntity> findById(Long userId);
}
