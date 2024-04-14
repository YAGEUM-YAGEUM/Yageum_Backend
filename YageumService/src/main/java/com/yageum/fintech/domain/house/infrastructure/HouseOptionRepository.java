package com.yageum.fintech.domain.house.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseOptionRepository extends JpaRepository<HouseOption, Long> {

    Optional<HouseOption> findByHouseId(Long houseId);

}
