package com.yageum.fintech.domain.match.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Matching, Long> {

    List<Matching> findByHouseHouseId(Long houseId);

    List<Matching> findByTenantTenantId(Long tenantId);

}
