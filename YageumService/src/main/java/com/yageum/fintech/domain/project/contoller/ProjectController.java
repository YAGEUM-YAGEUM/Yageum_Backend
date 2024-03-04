package com.yageum.fintech.domain.project.contoller;

import com.yageum.fintech.domain.project.dto.response.GetProjectListResponseDto;
import com.yageum.fintech.domain.project.dto.response.GetProjectResponseDto;
import com.yageum.fintech.domain.project.service.ProjectService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import com.yageum.fintech.domain.project.dto.reqeust.CreateProjectRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "프로젝트 CRUD API", description = "프로젝트 CRUD API")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;
    private final ResponseService responseService;

    //프로젝트 등록
    @Operation(summary = "프로젝트 생성")
    @PostMapping()
    public CommonResult createProject(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto) {
        projectService.createProject(createProjectRequestDto);
        return responseService.getSuccessfulResult();
    }
    //프로젝트 전체 조회
    @Operation(summary = "프로젝트 전체목록 조회")
    @GetMapping()
    public CommonResult getProjectList() {
        List<GetProjectListResponseDto> projectList = projectService.getProjectList();
        return responseService.getListResult(projectList);
    }

    @Operation(summary = "내가 속한 프로젝트 목록 조회")
    @GetMapping("/me")
    public CommonResult getMyProjectList() {
        List<GetProjectListResponseDto> myProjectList = projectService.getMyProjectList();
        return responseService.getListResult(myProjectList);
    }

    // 프로젝트 상세 조회
    @Operation(summary = "프로젝트 상세 조회")
    @GetMapping("/{projectId}")
    public CommonResult getProject(@PathVariable Long projectId) {
        GetProjectResponseDto project = projectService.getProject(projectId);
        return responseService.getSingleResult(project);
    }

    //프로젝트 수정
    @Operation(summary = "프로젝트 수정")
    @PutMapping("/{projectId}")
    public CommonResult updateProject(@PathVariable Long projectId,
                                      @Valid @RequestBody CreateProjectRequestDto projectUpdateRequestDto){
        projectService.updateProject(projectId, projectUpdateRequestDto);
        return responseService.getSuccessfulResult();
    }
    //프로젝트 삭제
    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/{projectId}")
    public CommonResult deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return responseService.getSuccessfulResult();
    }
    //프로젝트 완료 표시
    @Operation(summary = "프로젝트 완료 표시")
    @PutMapping("/{projectId}/finish")
    public CommonResult finishProject(@PathVariable Long projectId){
        projectService.finishProject(projectId);
        return responseService.getSuccessfulResult();
    }
}
