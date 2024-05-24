package com.yageum.fintech.domain.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existChatRoomByHouseIdAndMembers(Long houseId, Long creatorId, Long participantId);

    boolean existChatRoomByHouseIdAndMembersReverse(Long houseId, Long creatorId, Long participantId);

    @Query("SELECT c FROM ChatRoom c WHERE c.houseId = :houseId AND ((c.creatorId = :creatorId AND c.participantId = :participantId) OR (c.creatorId = :participantId AND c.participantId = :creatorId))")
    ChatRoom findChatRoomByHouseIdAndMembers(Long houseId, Long creatorId, Long participantId);

}
