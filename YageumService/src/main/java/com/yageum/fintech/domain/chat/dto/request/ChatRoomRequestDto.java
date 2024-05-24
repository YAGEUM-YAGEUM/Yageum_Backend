package com.yageum.fintech.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ChatRoomRequestDto {

    @NotNull
    private Long houseId;

    @NotNull
    private Long participantId;
}
