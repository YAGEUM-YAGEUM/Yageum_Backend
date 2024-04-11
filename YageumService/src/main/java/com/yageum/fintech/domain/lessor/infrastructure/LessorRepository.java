package com.yageum.fintech.domain.lessor.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessorRepository extends JpaRepository<Lessor, Long> {

    Optional<Lessor> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<Lessor> findByLessorId(Long lessorId);

}
