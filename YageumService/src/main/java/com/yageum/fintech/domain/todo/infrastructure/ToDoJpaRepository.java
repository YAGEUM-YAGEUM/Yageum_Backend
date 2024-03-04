package com.yageum.fintech.domain.todo.infrastructure;

import com.yageum.fintech.domain.project.infrastructure.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoJpaRepository extends JpaRepository<ToDoEntity, Long> {
    @Query("SELECT t FROM ToDoEntity t WHERE t.project = :project ORDER BY CASE WHEN t.todoEmergency = true AND t.isChecked = false THEN 1 WHEN t.todoEmergency = false AND t.isChecked = false THEN 2 WHEN t.todoEmergency = true AND t.isChecked = true THEN 3 ELSE 4 END")
    List<ToDoEntity> findByProjectAndOrderByConditions(Project project);
}
