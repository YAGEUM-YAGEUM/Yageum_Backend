package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.yageum.fintech.domain.chat.dto.response.ChatRoomResponseDto;
import com.yageum.fintech.domain.chat.infrastructure.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,  CustomChatRoomRepository {

    // 매물 글 기준으로 내가 만든 채팅방이 있는지 확인
    boolean existsByHouseIdAndCreatorIdAndParticipantId(Long houseId, Long creatorId, Long participantId);

    // 매물 글 기준으로 내가 참가자인 채팅방이 있는지 확인
    boolean existsByHouseIdAndParticipantIdAndCreatorId(Long houseId, Long creatorId, Long participantId);

    // 매물 글 기준으로 내가 참여하는 채팅방 찾아주는 메소드
    @Query("select c from ChatRoom c WHERE c.houseId = :houseId AND ((c.creatorId = :creatorId AND c.participantId = :participantId) OR (c.creatorId = :participantId AND c.participantId = :creatorId))")
    ChatRoom findChatRoomByHouseIdAndMembers(@Param("houseId") Long houseId, @Param("creatorId") Long creatorId, @Param("participantId") Long participantId);

 }
