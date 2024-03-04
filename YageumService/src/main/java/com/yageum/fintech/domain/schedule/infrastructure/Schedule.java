package com.yageum.fintech.domain.schedule.infrastructure;

import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public void update(CreateScheduleRequestDto updateScheduleRequestDto) {
        this.content = updateScheduleRequestDto.getContent();
        this.startDate = updateScheduleRequestDto.readStartDateAsLocalDateType();
        this.endDate = updateScheduleRequestDto.readEndDateAsLocalDateType();
    }
}
