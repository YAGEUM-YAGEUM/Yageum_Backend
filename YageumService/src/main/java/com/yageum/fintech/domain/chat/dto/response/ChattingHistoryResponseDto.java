package com.yageum.fintech.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChattingHistoryResponseDto {

    private String username;
    private List<ChatResponseDto> chatList;
}
