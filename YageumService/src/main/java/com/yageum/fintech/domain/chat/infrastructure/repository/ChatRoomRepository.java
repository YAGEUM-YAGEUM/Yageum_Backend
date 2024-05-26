package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.yageum.fintech.domain.chat.infrastructure.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,  CustomChatRoomRepository {

    // 매물 글 기준으로 내가 만든 채팅방이 있는지 확인
    boolean existChatRoomByHouseIdAndMembers(Long houseId, Long creatorId, Long participantId);

    // 매물 글 기준으로 내가 참가자인 채팅방이 있는지 확인
    boolean existChatRoomByHouseIdAndMembersReverse(Long houseId, Long creatorId, Long participantId);

    // 매물 글 기준으로 내가 참여하는 채팅방 찾아주는 메소드
    @Query("select c from ChatRoom c WHERE c.houseId = :houseId AND ((c.creatorId = :creatorId AND c.participantId = :participantId) OR (c.creatorId = :participantId AND c.participantId = :creatorId))")
    ChatRoom findChatRoomByHouseIdAndMembers(Long houseId, Long creatorId, Long participantId);

    // 내가 만든 채팅방 또는 내가 참여중인 채팅방을 전부 찾아주는 메서드
    @Query("select c from ChatRoom c where c.creatorId = :memberNo or c.participantId = :memberNo")
    List<ChatRoom> findChattingRoom(@Param("memberNo") Long memberNo);

}
