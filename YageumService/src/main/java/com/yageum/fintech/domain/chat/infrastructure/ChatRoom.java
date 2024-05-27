package com.yageum.fintech.domain.chat.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "chatRoom")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom { //mysql 에 저장할 ChatRoom entity

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_no")
    private Long chatRoomNo;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "participant_id")
    private Long participantId;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    public ChatRoom(Long chatRoomNo, Long creatorId, Long participantId, Long houseId, LocalDateTime regDate) {
        this.chatRoomNo = chatRoomNo;
        this.creatorId = creatorId;
        this.participantId = participantId;
        this.houseId = houseId;
        this.regDate = regDate;
    }

}

