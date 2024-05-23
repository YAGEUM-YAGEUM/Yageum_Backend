package com.yageum.fintech.domain.chat.dto;

import com.yageum.fintech.domain.chat.infrastructure.ChatContentType;
import com.yageum.fintech.domain.chat.infrastructure.Chatting;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private Long chattingId;

    @NotNull
    private Long chatRoomNo;

    private Long senderId;

    private String senderName;

    @NotNull
    private ChatContentType contentType;

    @NotNull
    private String content;

    private long sendTime;

    private Integer readCount;

    public void setSendTimeAndSender(LocalDateTime sendTime, Long senderId, String senderName, Integer readCount) {
        this.senderName = senderName;
        this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        this.senderId = senderId;
        this.readCount = readCount;
    }

    public void setId(Long chattingId) {
        this.chattingId = chattingId;
    }

    public Chatting convertEntity() {
        return Chatting.builder()
                .senderName(senderName)
                .senderId(senderId)
                .chatRoomNo(chatRoomNo)
                .contentType(contentType)
                .content(content)
                .sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                .readCount(readCount)
                .build();
    }
}