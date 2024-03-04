package com.yageum.fintech.domain.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoRequestDto {

    @Schema(description = "할일 내용", defaultValue = "할일 내용")
    private String todoContent;

    @Schema(description = "긴급 표시", defaultValue = "긴급 표시")
    private boolean todoEmergency;

}

