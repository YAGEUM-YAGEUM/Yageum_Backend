package com.yageum.fintech.domain.project.service;

import com.yageum.fintech.domain.project.dto.response.GetProjectListResponseDto;
import com.yageum.fintech.domain.project.dto.response.GetProjectResponseDto;
import com.yageum.fintech.domain.project.dto.response.GetProjectUserResponseDto;
import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.project.infrastructure.ProjectRepository;
import com.yageum.fintech.domain.project.infrastructure.ProjectUser;
import com.yageum.fintech.domain.project.infrastructure.Role;
import com.yageum.fintech.domain.user.dto.response.GetUserResponseDto;
import com.yageum.fintech.domain.user.service.UserServiceImpl2;
import com.yageum.fintech.global.config.jwtInterceptor.JwtContextHolder;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.domain.project.dto.reqeust.CreateProjectRequestDto;
import com.yageum.fintech.global.model.Exception.StartDateAfterEndDateException;
import com.yageum.fintech.global.model.Exception.UnauthorizedAccessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final UserServiceImpl2 userService;

    @Override
    @Transactional
    public void createProject(CreateProjectRequestDto createProjectRequestDto) {
        validateProjectPeriod(createProjectRequestDto);
        Project project = projectRepository.save(createProjectRequestDto.toEntity());

        Set<Long> memberIdList = createProjectRequestDto.getMemberIdList();
        memberIdList.remove(JwtContextHolder.getUserId());

        filterNonExistentMemberId(memberIdList);

        project.getProjectUserList().add(
                ProjectUser.builder()
                        .project(project)
                        .userId(JwtContextHolder.getUserId())
                        .role(Role.LEADER)
                        .build()
        );
        for(Long memberId : memberIdList)
            project.getProjectUserList().add(
                    ProjectUser.builder()
                    .project(project)
                    .userId(memberId)
                    .role(Role.MEMBER)
                    .build()
            );
    }

    @Override
    @Transactional
    public List<GetProjectListResponseDto> getProjectList() {
        List<GetProjectListResponseDto> projectList = projectRepository.findAll()
                .stream().map(GetProjectListResponseDto::from)
                .collect(Collectors.toList());
        if (projectList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        return projectList;
    }

    @Override
    public List<GetProjectListResponseDto> getMyProjectList() {
        List<GetProjectListResponseDto> myProjectList = projectRepository.findAllByUserId(JwtContextHolder.getUserId())
                .stream().map(GetProjectListResponseDto::from)
                .collect(Collectors.toList());
        if (myProjectList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);
        return myProjectList;
    }

    @Override
    public GetProjectResponseDto getProject(Long projectId) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        if(!project.isLeaderOrMember(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
        List<GetProjectUserResponseDto> getProjectUserResponseDtoList =
                getProjectUserResponseDtoList(project.getProjectUserList());
        return GetProjectResponseDto.from(project, getProjectUserResponseDtoList);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        projectRepository.delete(project);

    }

    @Override
    @Transactional
    public void updateProject(Long projectId, CreateProjectRequestDto projectUpdateRequestDto) {
        Project project = projectRepository.findByIdWithProjectUserList(projectId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        validateProjectPeriod(projectUpdateRequestDto);
        project.update(projectUpdateRequestDto);

        Set<Long> requestMemberIdList = projectUpdateRequestDto.getMemberIdList();
        requestMemberIdList.remove(JwtContextHolder.getUserId());
        // 요청 dto에 없지만 db에는 있는 팀원을 삭제
        deleteProjectUser(project, requestMemberIdList);

        filterNonExistentMemberId(requestMemberIdList);
        // 요청 dto에 있지만 db에는 없는 팀원을 추가
        addProjectUser(project, requestMemberIdList);

    }

    @Override
    @Transactional
    public void finishProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT));
        checkLeaderAuthorization(project);
        project.finish();

    }

    public void validateProjectPeriod(CreateProjectRequestDto createProjectRequestDto){
        LocalDate startDate = createProjectRequestDto.startDateAsLocalDateType();
        LocalDate endDate = createProjectRequestDto.finishDateAsLocalDateType();
        if(startDate.isAfter(endDate))
            throw new StartDateAfterEndDateException(ExceptionList.START_DATE_AFTER_END_DATE_EXCEPTION);
    }

    // 요청 dto에 있지만 db에는 없는 팀원을 추가
    private void addProjectUser(Project project, Set<Long> requestMemberIdList) {

        Set<Long> existingMemberIdList = project.getMemberIdList();
        for(Long requestMemberId : requestMemberIdList)
            if(!existingMemberIdList.contains(requestMemberId))
                project.getProjectUserList().add(
                        ProjectUser.builder()
                                .project(project)
                                .userId(requestMemberId)
                                .role(Role.MEMBER)
                                .build()
                );

    }

    // 요청 dto에 없지만 db에는 있는 팀원을 삭제
    private void deleteProjectUser(Project project, Set<Long> requestMemberIdList) {
        project.getProjectUserList()
                .removeIf(projectUser -> projectUser.getRole() != Role.LEADER &&
                        !requestMemberIdList.contains(projectUser.getUserId()));
    }

    private void checkLeaderAuthorization(Project project) {
        if(!project.isLeader(JwtContextHolder.getUserId()))
            throw new UnauthorizedAccessException(ExceptionList.UNAUTHORIZED_ACCESS);
    }

    private List<GetProjectUserResponseDto> getProjectUserResponseDtoList(List<ProjectUser> projectUserList) {
        List<GetProjectUserResponseDto> getProjectUserResponseDtoList = new ArrayList<>();
        for(ProjectUser projectUser : projectUserList){
            GetUserResponseDto getUserResponseDto = userService.getUser(projectUser.getUserId());
            if(getUserResponseDto==null) continue;
            GetProjectUserResponseDto getProjectUserResponseDto =
                    GetProjectUserResponseDto.from(getUserResponseDto, projectUser.getRole());
            getProjectUserResponseDtoList.add(getProjectUserResponseDto);
        }
        return getProjectUserResponseDtoList;
    }

    private void filterNonExistentMemberId(Set<Long> memberIdList) {
        memberIdList.removeIf(memberId -> userService.getUser(memberId) == null);
    }
}
