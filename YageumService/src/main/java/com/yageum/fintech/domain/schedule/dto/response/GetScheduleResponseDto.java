package com.yageum.fintech.domain.schedule.dto.response;

import com.yageum.fintech.domain.schedule.infrastructure.Schedule;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetScheduleResponseDto {
    private Long scheduleId;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    public static GetScheduleResponseDto from(Schedule schedule){
        return GetScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .content(schedule.getContent())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
