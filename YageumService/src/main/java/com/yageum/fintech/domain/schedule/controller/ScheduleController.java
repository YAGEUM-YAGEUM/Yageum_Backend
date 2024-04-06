package com.yageum.fintech.domain.schedule.controller;

import com.yageum.fintech.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.yageum.fintech.domain.schedule.dto.response.GetScheduleResponseDto;
import com.yageum.fintech.domain.schedule.service.ScheduleService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "일정 CRUD API", description = "일정 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin("*")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ResponseService responseService;

    //일정 등록
    @Operation(summary = "일정 등록하기", description = "특정 프로젝트 내에서 일정을 등록하는 API")
    @ResponseStatus(OK)
    @PostMapping("/projects/{projectId}/schedules")
    public CommonResult createSchedule(@PathVariable Long projectId, @Valid @RequestBody CreateScheduleRequestDto createScheduleRequestDto){
        scheduleService.createSchedule(projectId, createScheduleRequestDto);
        return responseService.getSuccessfulResult();
    }

    //일정 목록 조회
    @Operation(summary = "일정 목록 가져오기", description = "특정 프로젝트의 일정 목록을 가져오는 API")
    @ResponseStatus(OK)
    @GetMapping("/projects/{projectId}/schedules")
    public CommonResult getScheduleList(@PathVariable Long projectId) {
        List<GetScheduleResponseDto> scheduleList = scheduleService.getScheduleList(projectId);
        return responseService.getListResult(scheduleList);
    }

    //일정 하나 조회
    @Operation(summary = "일정 가져오기", description = "일정을 가져오는 API")
    @ResponseStatus(OK)
    @GetMapping("/schedules/{scheduleId}")
    public CommonResult getSchedule(@PathVariable Long scheduleId) {
        GetScheduleResponseDto schedule = scheduleService.getSchedule(scheduleId);
        return responseService.getSingleResult(schedule);
    }

    //일정 수정
    @Operation(summary = "일정 수정하기", description = "일정을 수정하는 API")
    @ResponseStatus(OK)
    @PutMapping("/schedules/{scheduleId}")
    public CommonResult updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody CreateScheduleRequestDto updateScheduleRequestDto) {
        scheduleService.updateSchedule(scheduleId, updateScheduleRequestDto);
        return responseService.getSuccessfulResult();
    }

    //일정 삭제
    @Operation(summary = "일정 삭제하기", description = "일정을 삭제하는 API")
    @ResponseStatus(OK)
    @DeleteMapping("/schedules/{scheduleId}")
    public CommonResult deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return responseService.getSuccessfulResult();
    }
}
