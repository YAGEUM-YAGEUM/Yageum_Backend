package com.yageum.fintech.domain.schedule.service;


import com.yageum.fintech.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.yageum.fintech.domain.schedule.dto.response.GetScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    void createSchedule(Long projectId, CreateScheduleRequestDto createScheduleRequestDto);
    List<GetScheduleResponseDto> getScheduleList(Long projectId);
    GetScheduleResponseDto getSchedule(Long scheduleIndex);
    void updateSchedule(Long scheduleIndex, CreateScheduleRequestDto updateScheduleRequestDto);
    void deleteSchedule(Long scheduleIndex);
}
