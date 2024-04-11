package com.yageum.fintech.domain.lessor.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessorProfileRepository extends JpaRepository<LessorProfile, Long> {

    Optional<LessorProfile> findByLessor_LessorId(Long lessorId);

    Optional<LessorProfile> findByProfileId(Long profileId);

}
