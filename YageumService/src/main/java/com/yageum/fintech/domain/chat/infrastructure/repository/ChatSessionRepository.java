package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.yageum.fintech.domain.chat.dto.request.ChatSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository extends CrudRepository<ChatSession, String> {

    List<ChatSession> findByChatroomNo(Long chatRoomNo);

    List<ChatSession> findByHouseId(Long houseId);

    Optional<ChatSession> findByChatroomNoAndUsername(Long chatRoomNo, String username);
}
