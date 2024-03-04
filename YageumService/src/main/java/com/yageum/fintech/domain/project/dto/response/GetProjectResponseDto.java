package com.yageum.fintech.domain.project.dto.response;

import com.yageum.fintech.domain.project.infrastructure.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectResponseDto {

    private Long projectId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate finishDate;
    private Boolean isFinished;
    private List<GetProjectUserResponseDto> leaderAndMemberList;

    public static GetProjectResponseDto from(Project project, List<GetProjectUserResponseDto> projectUserResponseDtoList){
        return GetProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .finishDate(project.getFinishDate())
                .description(project.getDescription())
                .isFinished(project.isChecked())
                .leaderAndMemberList(projectUserResponseDtoList)
                .build();
    }
}
