package com.yageum.fintech.domain.project;

import com.yageum.fintech.BaseApiTest;
import com.yageum.fintech.domain.project.dto.reqeust.CreateProjectRequestDto;
import com.mju.management.domain.project.infrastructure.*;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.global.config.jwtInterceptor.JwtContextHolder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ProjectApiTest extends BaseApiTest {

    @DisplayName("프로젝트 생성 성공: 팀원을 초대하지 않는 경우")
    @Test
    public void createProject_Success_No_Invite() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects";

        Set<Long> memberIdList = new HashSet<>();

        CreateProjectRequestDto createProjectRequestDto = CreateProjectRequestDto
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(projectStartDate)
                .finishDate(projectFinishDate)
                .memberIdList(memberIdList)
                .build();

        String requestBody = objectMapper.writeValueAsString(createProjectRequestDto);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<Project> projectList = projectRepository.findAll();

        assertThat(projectList.size()).isEqualTo(1);
        Project project = projectList.get(0);
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
        assertThat(project.getStartDate()).isEqualTo(projectStartDate);
        assertThat(project.getFinishDate()).isEqualTo(projectFinishDate);
        assertThat(projectUserRepository.findByProject(project).size())
                .isEqualTo(1);
        assertThat(projectUserRepository.findByProjectAndUserId(project, leaderId).isPresent())
                .isEqualTo(true);
    }

    @DisplayName("프로젝트 생성 성공: 팀원을 초대하는 경우")
    @Test
    public void createProject_Success_Invite() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(memberId);

        CreateProjectRequestDto createProjectRequestDto = CreateProjectRequestDto
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(projectStartDate)
                .finishDate(projectFinishDate)
                .memberIdList(memberIdList)
                .build();

        String requestBody = objectMapper.writeValueAsString(createProjectRequestDto);

        setUserServiceState(UserServiceState.NORMAL, memberId);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<Project> projectList = projectRepository.findAll();

        assertThat(projectList.size()).isEqualTo(1);
        Project project = projectList.get(0);
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
        assertThat(project.getStartDate()).isEqualTo(projectStartDate);
        assertThat(project.getFinishDate()).isEqualTo(projectFinishDate);
        assertThat(projectUserRepository.findByProjectAndUserId(project, leaderId).isPresent())
                .isEqualTo(true);
        assertThat(projectUserRepository.findByProjectAndUserId(project, memberId).isPresent())
                .isEqualTo(true);
    }

    @DisplayName("프로젝트 생성 성공: 유저서비스에 존재하지 않는 팀원을 초대하는 경우")
    @Test
    public void createProject_Success_Invite_NonExistentMember() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(memberId);

        CreateProjectRequestDto createProjectRequestDto = CreateProjectRequestDto
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(projectStartDate)
                .finishDate(projectFinishDate)
                .memberIdList(memberIdList)
                .build();

        String requestBody = objectMapper.writeValueAsString(createProjectRequestDto);

        setUserServiceState(UserServiceState.NON_EXISTENT_USER, memberId);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<Project> projectList = projectRepository.findAll();

        assertThat(projectList.size()).isEqualTo(1);
        Project project = projectList.get(0);
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
        assertThat(project.getStartDate()).isEqualTo(projectStartDate);
        assertThat(project.getFinishDate()).isEqualTo(projectFinishDate);
        assertThat(projectUserRepository.findByProjectAndUserId(project, leaderId).isPresent())
                .isEqualTo(true);
        assertThat(projectUserRepository.findByProjectAndUserId(project, memberId).isPresent())
                .isEqualTo(false);
    }

    @DisplayName("프로젝트 생성 성공: 팀원을 초대했는데 유저서비스에 장애가 있을때")
    @Test
    public void createProject_Success_Invite_Member_UserServiceError() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(memberId);

        CreateProjectRequestDto createProjectRequestDto = CreateProjectRequestDto
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(projectStartDate)
                .finishDate(projectFinishDate)
                .memberIdList(memberIdList)
                .build();

        String requestBody = objectMapper.writeValueAsString(createProjectRequestDto);

        setUserServiceState(UserServiceState.INTERNAL_SERVER_ERROR, memberId);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<Project> projectList = projectRepository.findAll();

        assertThat(projectList.size()).isEqualTo(1);
        Project project = projectList.get(0);
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getDescription()).isEqualTo(projectDescription);
        assertThat(project.getStartDate()).isEqualTo(projectStartDate);
        assertThat(project.getFinishDate()).isEqualTo(projectFinishDate);
        assertThat(projectUserRepository.findByProjectAndUserId(project, leaderId).isPresent())
                .isEqualTo(true);
        assertThat(projectUserRepository.findByProjectAndUserId(project, memberId).isPresent())
                .isEqualTo(false);
    }

    @DisplayName("프로젝트 생성 실패: 종료일이 시작일보다 앞섬")
    @Test
    public void createProject_Fail_StartDateAfterEndDate() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(memberId);

        CreateProjectRequestDto createProjectRequestDto = CreateProjectRequestDto
                .builder()
                .name(projectName)
                .description(projectDescription)
                .startDate(projectFinishDate)
                .finishDate(projectStartDate)
                .memberIdList(memberIdList)
                .build();

        String requestBody = objectMapper.writeValueAsString(createProjectRequestDto);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        result.andExpect(jsonPath("$.code").value(6002));
    }

    @DisplayName("프로젝트 전체목록 조회 성공")
    @Test
    public void getProjectList_Success() throws Exception {
        //given
        int count = 10;
        for(int i = 0; i < count; i++) createProject(leaderId);
        String url = "/api/projects";

        //when
        ResultActions result = mockMvc.perform(get(url)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").value(Matchers.hasSize(count)));
    }

    @DisplayName("프로젝트 전체목록 조회 실패: 프로젝트가 존재하지 않음")
    @Test
    public void getProjectList_Fail_NonExistentProject() throws Exception {
        //given
        String url = "/api/projects";

        //when
        ResultActions result = mockMvc.perform(get(url)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("내가 속한 프로젝트 목록 조회 성공")
    @Test
    public void getMyProjectList_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        int count = 10;
        for(int i = 0; i < count; i++) {
            createProjectUser(memberId, createProject(leaderId));
        }

        JwtContextHolder.setUserId(memberId);

        String url = "/api/projects/me";

        //when
        ResultActions result = mockMvc.perform(get(url)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").value(Matchers.hasSize(count)));
    }

    @DisplayName("내가 속한 프로젝트 목록 조회 실패: 프로젝트가 존재하지 않음")
    @Test
    public void getMyProjectList_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/me";

        //when
        ResultActions result = mockMvc.perform(get(url)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트 상세 조회 성공")
    @Test
    public void getProject_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);
        createProjectUser(memberId, project);

        String url = "/api/projects/{projectId}";

        setUserServiceState(UserServiceState.NORMAL, leaderId);
        setUserServiceState(UserServiceState.NORMAL, memberId);

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.data.name").value(projectName))
                .andExpect(jsonPath("$.data.description").value(projectDescription))
                .andExpect(jsonPath("$.data.startDate").value(projectStartDate))
                .andExpect(jsonPath("$.data.finishDate").value(projectFinishDate))
                .andExpect(jsonPath("$.data.leaderAndMemberList[0].userId").value(leaderId))
                .andExpect(jsonPath("$.data.leaderAndMemberList[1].userId").value(memberId));
    }

    @DisplayName("프로젝트 상세 조회 성공: 팀원이 유저서비스에 더 이상 존재하지 않음")
    @Test
    public void getProject_Success_NonExistentMember() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);
        createProjectUser(memberId, project);

        String url = "/api/projects/{projectId}";

        setUserServiceState(UserServiceState.NORMAL, leaderId);
        setUserServiceState(UserServiceState.NON_EXISTENT_USER, memberId);

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.data.name").value(projectName))
                .andExpect(jsonPath("$.data.description").value(projectDescription))
                .andExpect(jsonPath("$.data.startDate").value(projectStartDate))
                .andExpect(jsonPath("$.data.finishDate").value(projectFinishDate))
                .andExpect(jsonPath("$.data.leaderAndMemberList[0].userId").value(leaderId))
                .andExpect(jsonPath("$.data.leaderAndMemberList.length()").value(1));
    }

    @DisplayName("프로젝트 상세 조회 성공: 유저서비스에 장애가 있음")
    @Test
    public void getProject_Success_UserServiceError() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/projects/{projectId}";

        setUserServiceState(UserServiceState.INTERNAL_SERVER_ERROR, leaderId);

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.data.name").value(projectName))
                .andExpect(jsonPath("$.data.description").value(projectDescription))
                .andExpect(jsonPath("$.data.startDate").value(projectStartDate))
                .andExpect(jsonPath("$.data.finishDate").value(projectFinishDate))
                .andExpect(jsonPath("$.data.leaderAndMemberList.size()").value(0));
    }

    @DisplayName("프로젝트 상세 조회 실패: 프로젝트가 존재하지 않음")
    @Test
    public void getProject_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트 상세 조회 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void getProject_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Long projectId = createProject(leaderId).getProjectId();

        String url = "/api/projects/{projectId}";

        //when
        ResultActions result = mockMvc.perform(get(url, projectId)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트 수정 성공")
    @Test
    public void updateProject_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project createdProject = createProject(leaderId);
        createProjectUser(memberId, createdProject);
        Long projectId = createdProject.getProjectId();

        String url = "/api/projects/{projectId}";

        String name = "스튜디오아이 프로젝트";
        String description = "스튜디오아이 프로젝트입니다.";
        String startDate = "2023-10-01";
        String finishDate = "2023-11-11";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(outsiderId);

        CreateProjectRequestDto updateProjectRequestsDto = CreateProjectRequestDto
                .builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .finishDate(finishDate)
                .memberIdList(memberIdList)
                .build();

        setUserServiceState(UserServiceState.NORMAL, outsiderId);

        //when
        ResultActions result = mockMvc.perform(put(url, projectId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateProjectRequestsDto)));

        //then
        result.andExpect(jsonPath("$.code").value(200));

        Project project = projectRepository.findById(projectId).get();

        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(description);
        assertThat(project.getStartDate()).isEqualTo(startDate);
        assertThat(project.getFinishDate()).isEqualTo(finishDate);
        assertThat(projectUserRepository.findByProjectAndUserId(project, leaderId).isPresent())
                .isEqualTo(true);
        assertThat(projectUserRepository.findByProjectAndUserId(project, outsiderId).isPresent())
                .isEqualTo(true);
    }

    @DisplayName("프로젝트 수정 실패: 프로젝트가 존재하지 않음")
    @Test
    public void updateProject_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}";

        String name = "스튜디오아이 프로젝트";
        String description = "스튜디오아이 프로젝트입니다.";
        String startDate = "2023-10-01";
        String finishDate = "2023-11-11";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(outsiderId);

        CreateProjectRequestDto updateProjectRequestsDto = CreateProjectRequestDto
                .builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .finishDate(finishDate)
                .memberIdList(memberIdList)
                .build();

        //when
        ResultActions result = mockMvc.perform(put(url, 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateProjectRequestsDto)));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트 수정 실패: 요청자가 프로젝트 팀장이 아님")
    @Test
    public void updateProject_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(memberId);

        Project createdProject = createProject(leaderId);
        createProjectUser(memberId, createdProject);
        Long projectId = createdProject.getProjectId();

        String url = "/api/projects/{projectId}";

        String name = "스튜디오아이 프로젝트";
        String description = "스튜디오아이 프로젝트입니다.";
        String startDate = "2023-10-01";
        String finishDate = "2023-11-11";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(outsiderId);

        CreateProjectRequestDto updateProjectRequestsDto = CreateProjectRequestDto
                .builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .finishDate(finishDate)
                .memberIdList(memberIdList)
                .build();

        //when
        ResultActions result = mockMvc.perform(put(url, projectId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateProjectRequestsDto)));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트 수정 실패: 종료일이 시작일보다 앞섬")
    @Test
    public void updateProject_Fail_StartDateAfterEndDate() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project createdProject = createProject(leaderId);
        createProjectUser(memberId, createdProject);
        Long projectId = createdProject.getProjectId();

        String url = "/api/projects/{projectId}";

        String name = "스튜디오아이 프로젝트";
        String description = "스튜디오아이 프로젝트입니다.";
        String startDate = "2023-10-01";
        String finishDate = "2023-11-11";

        Set<Long> memberIdList = new HashSet<>();
        memberIdList.add(outsiderId);

        CreateProjectRequestDto updateProjectRequestsDto = CreateProjectRequestDto
                .builder()
                .name(name)
                .description(description)
                .startDate(finishDate)
                .finishDate(startDate)
                .memberIdList(memberIdList)
                .build();

        //when
        ResultActions result = mockMvc.perform(put(url, projectId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateProjectRequestsDto)));

        //then
        result.andExpect(jsonPath("$.code").value(6002));
    }

    @DisplayName("프로젝트 삭제 성공")
    @Test
    public void deleteProject_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/projects/{projectId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, project.getProjectId()));

        //then
        result.andExpect(jsonPath("$.code").value(200));
        assertThat(projectRepository.findById(project.getProjectId())).isEmpty();
    }

    @DisplayName("프로젝트 삭제 실패: 프로젝트가 존재하지 않음")
    @Test
    public void deleteProject_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, 1));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트 삭제 실패: 요청자가 프로젝트 팀장이 아님")
    @Test
    public void deleteProject_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(memberId);

        Project project = createProject(leaderId);
        createProjectUser(memberId, project);
        Long projectId = project.getProjectId();

        String url = "/api/projects/{projectId}";

        //when
        ResultActions result = mockMvc.perform(delete(url, projectId));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트 완료 성공")
    @Test
    public void finishProject_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Long projectId = createProject(leaderId).getProjectId();

        String url = "/api/projects/{projectId}/finish";

        //when
        ResultActions result = mockMvc.perform(put(url, projectId));

        //then
        result.andExpect(jsonPath("$.code").value(200));
        assertThat(projectRepository.findById(projectId).get().isChecked()).isTrue();
    }

    @DisplayName("프로젝트 완료 실패: 프로젝트가 존재하지 않음")
    @Test
    public void finishProject_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}/finish";

        //when
        ResultActions result = mockMvc.perform(put(url, 1));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트 완료 실패: 요청자가 프로젝트 팀장이 아님")
    @Test
    public void finishProject_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(memberId);

        Long projectId = createProject(leaderId).getProjectId();

        String url = "/api/projects/{projectId}/finish";

        //when
        ResultActions result = mockMvc.perform(put(url, projectId));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

}
