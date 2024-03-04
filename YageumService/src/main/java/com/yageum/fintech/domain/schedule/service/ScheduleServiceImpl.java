package com.yageum.fintech.domain.schedule.service;



import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.project.infrastructure.ProjectRepository;
import com.yageum.fintech.domain.project.infrastructure.ProjectUserRepository;
import com.yageum.fintech.domain.schedule.dto.response.GetScheduleResponseDto;
import com.yageum.fintech.domain.schedule.infrastructure.Schedule;
import com.yageum.fintech.global.config.jwtInterceptor.JwtContextHolder;
import com.mju.management.global.model.Exception.*;
import com.yageum.fintech.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.yageum.fintech.domain.schedule.infrastructure.ScheduleRepository;
import com.yageum.fintech.global.model.Exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    @Override
    public void createSchedule(Long projectId, CreateScheduleRequestDto createScheduleRequestDto) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
        validateSchedule(createScheduleRequestDto, project);
        scheduleRepository.save(createScheduleRequestDto.toEntity(project));
    }

    @Override
    public List<GetScheduleResponseDto> getScheduleList(Long projectId) {
        Project project = projectRepository.findByIdWithScheduleList(projectId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        projectUserRepository.findByProjectAndUserId(project, JwtContextHolder.getUserId())
                .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
        List<GetScheduleResponseDto> scheduleList = project.getScheduleList()
                .stream()
                .map(schedule -> GetScheduleResponseDto.from(schedule))
                .collect(Collectors.toList());
        if (scheduleList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_SCHEDULELIST);
        return scheduleList;
    }

    @Override
    public GetScheduleResponseDto getSchedule(Long scheduleId) {
        Schedule schedule =  scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_SCHEDULE));
        projectUserRepository.findByProjectAndUserId(schedule.getProject(), JwtContextHolder.getUserId())
                .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
        return GetScheduleResponseDto.from(schedule);
    }

    @Override
    @Transactional
    public void updateSchedule(Long scheduleId, CreateScheduleRequestDto updateScheduleRequestDto) {
        Schedule schedule =  scheduleRepository.findByIdWithProject(scheduleId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_SCHEDULE));
        projectUserRepository.findByProjectAndUserId(schedule.getProject(), JwtContextHolder.getUserId())
                .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
        validateSchedule(updateScheduleRequestDto, schedule.getProject());
        schedule.update(updateScheduleRequestDto);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_SCHEDULE));
        projectUserRepository.findByProjectAndUserId(schedule.getProject(), JwtContextHolder.getUserId())
                .orElseThrow(() -> new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS));
        scheduleRepository.delete(schedule);
    }

    public void validateSchedule(CreateScheduleRequestDto createScheduleRequestDto, Project project){
        LocalDate startDate = createScheduleRequestDto.readStartDateAsLocalDateType();
        LocalDate endDate = createScheduleRequestDto.readEndDateAsLocalDateType();
        if(startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException(ExceptionList.START_DATE_AFTER_END_DATE_EXCEPTION);
        if(startDate.isBefore(project.getStartDate()) || endDate.isAfter(project.getFinishDate()))
            throw new OutOfProjectScheduleRangeException(ExceptionList.OUT_OF_PROJECT_SCHEDULE_RANGE);
    }
}
