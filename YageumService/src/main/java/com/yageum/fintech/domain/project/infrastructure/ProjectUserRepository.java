package com.yageum.fintech.domain.project.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    Optional<ProjectUser> findByProjectAndUserId(Project project, Long userId);

    List<ProjectUser> findByProject(Project project);
}
