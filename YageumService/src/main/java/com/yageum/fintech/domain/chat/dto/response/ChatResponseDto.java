package com.yageum.fintech.domain.chat.dto.response;

import com.yageum.fintech.domain.chat.infrastructure.ChatContentType;
import com.yageum.fintech.domain.chat.infrastructure.Chatting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZoneId;

@Getter
@ToString
@AllArgsConstructor
public class ChatResponseDto { //Chatting 응답 객체
    private String chattingId;
    private Long chatRoomNo;
    private Long senderId;
    private String senderName;
    private ChatContentType contentType;
    private String content;
    private long sendDate;
    private long readCount;
    private boolean isMine;

    public ChatResponseDto(Chatting chatting, Long memberNo) {
        this.chattingId = chatting.getId();
        this.chatRoomNo = chatting.getChatRoomNo();
        this.senderId = chatting.getSenderId();
        this.senderName = chatting.getSenderName();
        this.contentType = chatting.getContentType();
        this.content = chatting.getContent();
        this.sendDate = chatting.getSendDate();
        this.readCount = chatting.getReadCount();
        this.isMine = chatting.getSenderId().equals(memberNo);
    }
}
