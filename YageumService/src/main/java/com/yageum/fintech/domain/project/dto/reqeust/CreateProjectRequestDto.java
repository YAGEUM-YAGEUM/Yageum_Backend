package com.yageum.fintech.domain.project.dto.reqeust;

import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.InvalidDateFormatException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequestDto {

    @NotBlank(message = "포르젝트명을 입력해주세요.")
    @Size(max = 255, message = "255자 이하로 입력해주세요.")
    @Schema(description = "프로젝트명", defaultValue = "소코아 프로젝트")
    private String name;

    @NotBlank(message = "프로젝트 설명을 입력해주세요.")
    @Size(max = 255, message = "255자 이하로 입력해주세요.")
    @Schema(description = "프로젝트 설명", defaultValue = "소코아 프로젝트입니다.")
    private String description;

    @NotBlank(message = "프로젝트 시작일을 입력해주세요.")
    @Schema(description = "프로젝트 시작일", defaultValue = "2023-09-01")
    private String startDate;

    @NotBlank(message = "프로젝트 종료일을 입력해주세요.")
    @Schema(description = "프로젝트 종료일", defaultValue = "2023-12-15")
    private String finishDate;

    @Schema(description = "프로젝트 팀원 아이디 목록", defaultValue = "[2, 3, 4]")
    private Set<Long> memberIdList = new HashSet<>();

    public Project toEntity(){
        return Project.builder()
                .name(name)
                .description(description)
                .startDate(startDateAsLocalDateType())
                .finishDate(finishDateAsLocalDateType())
                .build();
    }

    public LocalDate startDateAsLocalDateType(){
        return convertStringToLocalDate(startDate);
    }

    public LocalDate finishDateAsLocalDateType(){
        return convertStringToLocalDate(finishDate);
    }

    public LocalDate convertStringToLocalDate(String date){
        try{
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        }catch (DateTimeParseException e){
            throw new InvalidDateFormatException(ExceptionList.INVALID_DATE_FORMAT);
        }
    }
}