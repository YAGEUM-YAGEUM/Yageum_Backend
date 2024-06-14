package com.yageum.fintech.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoomRequestDto {

    @NotNull
    private Long houseId;

    @NotNull
    private Long participantId;
}
