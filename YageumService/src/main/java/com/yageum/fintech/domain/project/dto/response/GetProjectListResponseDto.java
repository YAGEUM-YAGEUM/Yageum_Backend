package com.yageum.fintech.domain.project.dto.response;

import com.yageum.fintech.domain.project.infrastructure.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectListResponseDto {

    private Long projectId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate finishDate;
    private Boolean isFinished;


    public static GetProjectListResponseDto from(Project project){
        return GetProjectListResponseDto.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .finishDate(project.getFinishDate())
                .description(project.getDescription())
                .isFinished(project.isChecked())
                .build();
    }
}
