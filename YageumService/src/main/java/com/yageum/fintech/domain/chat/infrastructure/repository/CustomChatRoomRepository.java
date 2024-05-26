package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.yageum.fintech.domain.chat.dto.response.ChatRoomResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomChatRoomRepository {

    List<ChatRoomResponseDto> getChatRoomList(@Param("memberNo") Long memberNo);

}
