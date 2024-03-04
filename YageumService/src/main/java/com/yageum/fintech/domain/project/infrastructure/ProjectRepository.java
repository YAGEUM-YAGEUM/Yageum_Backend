package com.yageum.fintech.domain.project.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	@Query("select p from Project p left join fetch p.scheduleList where p.projectId = :projectId")
	Optional<Project> findByIdWithScheduleList(@Param("projectId") Long projectId);

	@Query("select p from Project p left join fetch p.projectUserList where p.projectId = :projectId")
	Optional<Project> findByIdWithProjectUserList(@Param("projectId") Long projectId);

	@Query("SELECT p FROM Project p WHERE EXISTS (SELECT pu FROM p.projectUserList pu WHERE pu.userId = :userId)")
	List<Project> findAllByUserId(@Param("userId") Long userId);

}
