package com.yageum.fintech.domain.todo;

import com.yageum.fintech.BaseApiTest;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.todo.dto.ToDoRequestDto;
import com.yageum.fintech.domain.todo.infrastructure.ToDoEntity;
import com.yageum.fintech.domain.todo.infrastructure.ToDoJpaRepository;
import com.yageum.fintech.global.config.jwt.jwtInterceptor.JwtContextHolder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ToDoApiTest extends BaseApiTest {

    @Autowired
    private ToDoJpaRepository toDoRepository;

    private final String toDoContent = "소코아 프로젝트 스프린트 #1";
    private final Boolean toDoEmergency = false;

    private ToDoEntity createToDo(Project project){
        return toDoRepository.save(ToDoEntity
                .builder()
                .project(project)
                .todoContent(toDoContent)
                .todoEmergency(toDoEmergency)
                .build()
        );
    }

    @DisplayName("할일 생성 성공")
    @Test
    public void createToDo_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .todoEmergency(toDoEmergency)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(200));

        List<ToDoEntity> toDoList = toDoRepository.findAll();
        assertThat(toDoList.size()).isEqualTo(1);
        ToDoEntity toDoEntity = toDoList.get(0);
        assertThat(toDoEntity.getTodoContent()).isEqualTo(toDoContent);
        assertThat(toDoEntity.isTodoEmergency()).isEqualTo(toDoEmergency);
    }

    @Test
    @DisplayName("할일 생성 실패: 프로젝트가 존재하지 않음")
    public void createToDo_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .todoEmergency(toDoEmergency)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(post(url, 1)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @Test
    @DisplayName("할일 생성 실패: 요청자가 프로젝트 소속이 아님")
    public void createSchedule_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .todoEmergency(toDoEmergency)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(post(url, project.getProjectId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트의 할일 목록 조회 성공")
    @Test
    public void getToDoList_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        int count = 10;
        for(int i = 0; i < count; i++) createToDo(project);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").value(Matchers.hasSize(count)));
    }

    @DisplayName("프로젝트의 할일 목록 조회 실패: 프로젝트가 존재하지 않음")
    @Test
    public void getToDoList_Fail_NonExistentProject() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5008));
    }

    @DisplayName("프로젝트의 할일 목록 조회 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void getTodoList_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        int count = 10;
        for(int i = 0; i < count; i++) createToDo(project);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("프로젝트의 할일 목록 조회 실패: 일정이 존재하지 않음")
    @Test
    public void getToDoList_Fail_NonExistentSchedule() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/projects/{projectId}/todo";

        //when
        ResultActions result = mockMvc.perform(get(url, project.getProjectId())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5005));
    }

    @DisplayName("할일 조회 성공")
    @Test
    public void getToDo_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.todoIndex").value(toDoEntity.getTodoIndex()))
                .andExpect(jsonPath("$.data.todoContent").value(toDoEntity.getTodoContent()))
                .andExpect(jsonPath("$.data.todoEmergency").value(toDoEntity.isTodoEmergency()));
    }

    @DisplayName("할일 조회 실패: 할일이 존재하지 않음")
    @Test
    public void getToDo_Fail_NonExistentToDo() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5005));
    }

    @DisplayName("할일 조회 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void getToDo_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("할일 수정 성공")
    @Test
    public void updateToDo_Success() throws Exception {
        //given
        final String toDoContent = "스튜디오 프로젝트 스프린트 #212";

        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(put(url, toDoEntity.getTodoIndex())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(200));

        ToDoEntity createdTodo = toDoRepository.findById(toDoEntity.getTodoIndex()).get();
        assertThat(createdTodo.getTodoContent()).isEqualTo(toDoContent);
    }

    @DisplayName("할일 수정 실패: 할일이 존재하지 않음")
    @Test
    public void updateToDo_Fail_NonExistentSchedule() throws Exception {
        final String toDoContent = "스튜디오 프로젝트 스프린트 #212";
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(put(url, 1)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(5005));
    }

    @DisplayName("할일 수정 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void updateToDo_Fail_UnauthorizedAccess() throws Exception {
        //given
        final String toDoContent = "스튜디오 프로젝트 스프린트 #212";

        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        ToDoRequestDto toDoRequestDto = ToDoRequestDto
                .builder()
                .todoContent(toDoContent)
                .build();

        String requestBody = objectMapper.writeValueAsString(toDoRequestDto);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(put(url, toDoEntity.getTodoIndex())
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("할일 삭제 성공")
    @Test
    public void deleteToDo_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(delete(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200));
        assertThat(toDoRepository.findById(toDoEntity.getTodoIndex()).isEmpty()).isTrue();
    }

    @DisplayName("할일 삭제 실패: 할일이 존재하지 않음")
    @Test
    public void deleteToDo_Fail_NonExistentSchedule() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(delete(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5005));
    }

    @DisplayName("할일 삭제 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void deleteToDo_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(delete(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }

    @DisplayName("할일 완료 성공")
    @Test
    public void finishToDo_Success() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/finish/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(200));
        assertThat(toDoRepository.findById(toDoEntity.getTodoIndex()).get().isChecked()).isTrue();
    }

    @DisplayName("할일 완료 실패: 할일이 존재하지 않음")
    @Test
    public void finishToDo_Fail_NonExistentToDo() throws Exception {
        //given
        JwtContextHolder.setUserId(leaderId);

        Project project = createProject(leaderId);

        String url = "/api/todo/finish/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, 1)
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(5005));
    }

    @DisplayName("할일 완료 실패: 요청자가 프로젝트 소속이 아님")
    @Test
    public void finishToDo_Fail_UnauthorizedAccess() throws Exception {
        //given
        JwtContextHolder.setUserId(outsiderId);

        Project project = createProject(leaderId);

        ToDoEntity toDoEntity = createToDo(project);

        String url = "/api/todo/finish/{todoIndex}";

        //when
        ResultActions result = mockMvc.perform(get(url, toDoEntity.getTodoIndex())
                .accept(APPLICATION_JSON_VALUE));

        //then
        result.andExpect(jsonPath("$.code").value(8000));
    }
}
