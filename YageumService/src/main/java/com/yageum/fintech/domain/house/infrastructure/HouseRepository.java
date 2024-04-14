package com.yageum.fintech.domain.house.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByHouseId(Long houseId);

    List<House> findByLessorId(Long lessorId);

}
