package com.yageum.fintech.domain.schedule.infrastructure;

import com.yageum.fintech.domain.project.infrastructure.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByProject(Project project);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.project WHERE s.scheduleId = :scheduleId")
    Optional<Schedule> findByIdWithProject(@Param("scheduleId") Long scheduleId);
}
