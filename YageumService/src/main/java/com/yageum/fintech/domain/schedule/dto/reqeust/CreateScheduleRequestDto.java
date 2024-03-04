package com.yageum.fintech.domain.schedule.dto.reqeust;

import com.yageum.fintech.domain.project.infrastructure.Project;
import com.yageum.fintech.domain.schedule.infrastructure.Schedule;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.InvalidDateFormatException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScheduleRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 255, message = "255자 이하로 입력해주세요.")
    @Schema(description = "일정 내용", defaultValue = "소코아 프로젝트 스프린트 #1")
    private String content;

    @NotBlank(message = "일정 시작일을 입력해주세요.")
    @Schema(description = "일정 시작일", defaultValue = "2023-11-01")
    private String startDate;

    @NotBlank(message = "일정 종료일을 입력해주세요.")
    @Schema(description = "일정 종료일", defaultValue = "2023-11-07")
    private String endDate;

    public Schedule toEntity(Project project){
        return Schedule.builder()
                .project(project)
                .content(content)
                .startDate(readStartDateAsLocalDateType())
                .endDate(readEndDateAsLocalDateType())
                .build();
    }

    public LocalDate readStartDateAsLocalDateType(){
        return convertStringToLocalDate(startDate);
    }

    public LocalDate readEndDateAsLocalDateType(){
        return convertStringToLocalDate(endDate);
    }

    public LocalDate convertStringToLocalDate(String date){
        try{
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        }catch (DateTimeParseException e){
            throw new InvalidDateFormatException(ExceptionList.INVALID_DATE_FORMAT);
        }
    }

}
