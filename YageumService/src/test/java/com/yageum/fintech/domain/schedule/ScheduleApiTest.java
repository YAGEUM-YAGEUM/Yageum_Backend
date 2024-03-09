package com.yageum.fintech.domain.schedule;

import com.yageum.fintech.BaseApiTest;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.yageum.fintech.domain.schedule.infrastructure.Schedule;
import com.yageum.fintech.domain.schedule.infrastructure.ScheduleRepository;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ScheduleApiTest extends BaseApiTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    private final String scheduleContent = "소코아 프로젝트 스프린트 #1";
    private final String scheduleStartDate = "2023-11-01";
    private final String scheduleEndDate = "2023-11-07";

    private Schedule createSchedule(Project project){
        return scheduleRepository.save(Schedule
                .builder()
                .project(project)
                .content(scheduleContent)
                .startDate(LocalDate.parse(scheduleStartDate))
                .endDate(LocalDate.parse(scheduleEndDate))
                .build()
        );
    }

    @DisplayName("일정 생성 성공")
    @Test
    public void createSchedule_Success() throws Exception {
        //given

        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        CreateScheduleRequestDto createScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(createScheduleRequestDto);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList.size()).isEqualTo(1);
        Schedule schedule = scheduleList.get(0);
        assertThat(schedule.getContent()).isEqualTo(scheduleContent);
        assertThat(schedule.getStartDate()).isEqualTo(scheduleStartDate);
        assertThat(schedule.getEndDate()).isEqualTo(scheduleEndDate);
    }

    @Test
    @DisplayName("일정 생성 실패: 프로젝트가 존재하지 않음")
    public void createSchedule_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        CreateScheduleRequestDto createScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(createScheduleRequestDto);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(post(url, 1)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
        assertThat(scheduleRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("일정 생성 실패: 요청자가 프로젝트 소속이 아님")
    public void createSchedule_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        CreateScheduleRequestDto createScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(createScheduleRequestDto);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
        assertThat(scheduleRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("일정 생성 실패: 종료일이 시작일보다 앞섬")
    public void createSchedule_Fail_StartDateAfterEndDate() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        CreateScheduleRequestDto createScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleEndDate)
                .endDate(scheduleStartDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(createScheduleRequestDto);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(6002));
        assertThat(scheduleRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("일정 생성 실패: 일정이 프로젝트 기간에 벗어남")
    public void createSchedule_Fail_OutOfProjectScheduleRange() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        CreateScheduleRequestDto createScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate("2030-10-01")
                .build();

        String requestBody = objectMapper.writeValueAsString(createScheduleRequestDto);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(6004));
        assertThat(scheduleRepository.findAll()).isEmpty();
    }

    @DisplayName("프로젝트의 일정 목록 조회 성공")
    @Test
    public void getScheduleList_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        int count = 10;
        for(int i = 0; i < count; i++) createSchedule(project);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").value(Matchers.hasSize(count)));
    }

    @DisplayName("프로젝트의 일정 목록 조회 실패: 프로젝트가 존재하지 않음")
    @Test
    public void getScheduleList_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트의 일정 목록 조회 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void getScheduleList_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        int count = 10;
        for(int i = 0; i < count; i++) createSchedule(project);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트의 일정 목록 조회 실패: 일정이 존재하지 않음")
    @Test
    public void getScheduleList_Fail_NonExistentSchedule() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/projects/{projectId}/schedules";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(6001));
    }

    @DisplayName("일정 조회 성공")
    @Test
    public void getSchedule_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        Schedule schedule =  createSchedule(project);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(get(url, schedule.getScheduleId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.scheduleId").value(schedule.getScheduleId()))
                .andExpect(jsonPath("$.data.content").value(schedule.getContent()))
                .andExpect(jsonPath("$.data.startDate").value(scheduleStartDate))
                .andExpect(jsonPath("$.data.endDate").value(scheduleEndDate));
    }

    @DisplayName("일정 조회 실패: 일정이 존재하지 않음")
    @Test
    public void getSchedule_Fail_NonExistentSchedule() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(6000));
    }

    @DisplayName("일정 조회 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void getSchedule_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        Schedule schedule =  createSchedule(project);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(get(url, schedule.getScheduleId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("일정 수정 성공")
    @Test
    public void updateSchedule_Success() throws Exception {
        //given
        final String scheduleContent = "스튜디오 프로젝트 스프린트 #3";
        final String scheduleStartDate = "2023-11-02";
        final String scheduleEndDate = "2023-11-08";

        JwtContextHolder.setUserId(leaderId);

        Schedule schedule = createSchedule(createProject(leaderId));

        CreateScheduleRequestDto updateScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestDto);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(put(url, schedule.getScheduleId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(200));

        Schedule createdSchedule = scheduleRepository.findById(schedule.getScheduleId()).get();
        assertThat(createdSchedule.getContent()).isEqualTo(scheduleContent);
        assertThat(createdSchedule.getStartDate()).isEqualTo(scheduleStartDate);
        assertThat(createdSchedule.getEndDate()).isEqualTo(scheduleEndDate);
    }

    @DisplayName("일정 수정 실패: 일정이 존재하지 않음")
    @Test
    public void updateSchedule_Fail_NonExistentSchedule() throws Exception {
        //given
        final String scheduleContent = "스튜디오 프로젝트 스프린트 #3";
        final String scheduleStartDate = "2023-11-02";
        final String scheduleEndDate = "2023-11-08";

        JwtContextHolder.setUserId(leaderId);

        CreateScheduleRequestDto updateScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestDto);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(put(url, 1)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(6000));
    }

    @DisplayName("일정 수정 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void updateSchedule_Fail_UnauthorizedAccess() throws Exception {
        //given
        final String scheduleContent = "스튜디오 프로젝트 스프린트 #3";
        final String scheduleStartDate = "2023-11-02";
        final String scheduleEndDate = "2023-11-08";

        JwtContextHolder.setUserId(outsiderId);

        Schedule schedule = createSchedule(createProject(leaderId));

        CreateScheduleRequestDto updateScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestDto);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(put(url, schedule.getScheduleId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("일정 수정 실패: 종료일이 시작일보다 앞섬")
    @Test
    public void updateSchedule_Fail_StartDateAfterEndDate() throws Exception {
        //given
        final String scheduleContent = "스튜디오 프로젝트 스프린트 #3";
        final String scheduleStartDate = "2023-11-02";
        final String scheduleEndDate = "2023-11-08";

        JwtContextHolder.setUserId(leaderId);

        Schedule schedule = createSchedule(createProject(leaderId));

        CreateScheduleRequestDto updateScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleEndDate)
                .endDate(scheduleStartDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestDto);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(put(url, schedule.getScheduleId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(6002));
    }

    @DisplayName("일정 수정 실패: 프로젝트의 기간에 벗어남")
    @Test
    public void updateSchedule_Fail_OutOfProjectScheduleRange() throws Exception {
        //given
        final String scheduleContent = "스튜디오 프로젝트 스프린트 #3";
        final String scheduleStartDate = "2023-11-02";
        final String scheduleEndDate = "2050-11-08";

        JwtContextHolder.setUserId(leaderId);

        Schedule schedule = createSchedule(createProject(leaderId));

        CreateScheduleRequestDto updateScheduleRequestDto = CreateScheduleRequestDto
                .builder()
                .content(scheduleContent)
                .startDate(scheduleStartDate)
                .endDate(scheduleEndDate)
                .build();

        String requestBody = objectMapper.writeValueAsString(updateScheduleRequestDto);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(put(url, schedule.getScheduleId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(6004));
    }

    @DisplayName("일정 삭제 성공")
    @Test
    public void deleteSchedule_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        Schedule schedule =  createSchedule(project);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, schedule.getScheduleId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200));
        assertThat(scheduleRepository.findById(schedule.getScheduleId()).isEmpty()).isTrue();
    }

    @DisplayName("일정 삭제 실패: 일정이 존재하지 않음")
    @Test
    public void deleteSchedule_Fail_NonExistentSchedule() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(6000));
    }

    @DisplayName("일정 삭제 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void deleteSchedule_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        Schedule schedule =  createSchedule(project);

        String url = "/api/schedules/{scheduleId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, schedule.getScheduleId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }
}
