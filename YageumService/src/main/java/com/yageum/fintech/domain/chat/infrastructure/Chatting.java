package com.yageum.fintech.domain.chat.infrastructure;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatting")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatting { //Mongo DB에 메시지 저장

    @Id
    private Long chattingId;
    private Long chatRoomNo;
    private Long senderId;
    private Long houseId;
    private String senderName;
    private ChatContentType contentType;
    private String content;
    private long sendDate;
    private long readCount;

    @Builder
    public Chatting(Long chattingId, Long chatRoomNo, Long senderId, Long houseId, String senderName,
                    ChatContentType contentType, String content, long sendDate, long readCount) {
        this.chattingId = chattingId;
        this.chatRoomNo = chatRoomNo;
        this.houseId = houseId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.contentType = contentType;
        this.content = content;
        this.sendDate = sendDate;
        this.readCount = readCount;
    }
}
