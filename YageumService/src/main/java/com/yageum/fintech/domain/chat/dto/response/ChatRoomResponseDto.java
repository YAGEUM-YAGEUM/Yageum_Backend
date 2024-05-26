package com.yageum.fintech.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Long chatRoomNo;

    private Long creatorId;

    private Long participantId;

    private Long houseId;

    private long regDate;

    private Attendee attendee; // 본인 제외 참여한 사람의 정보

    private LatestMessage latestMessage;

    private long unReadCount;

    public ChatRoomResponseDto(Long chatRoomNo, Long creatorId, Long participantId, Long houseId, LocalDateTime regDate) {
        this.chatRoomNo = chatRoomNo;
        this.creatorId = creatorId;
        this.participantId = participantId;
        this.houseId = houseId;
        this.regDate = regDate.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }

    public void setUnReadCount(long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public void setLatestMessage(LatestMessage latestMessage) {
        this.latestMessage = latestMessage;
    }

    // 본인 제외 참여한 사람의 정보
    @Getter
    @AllArgsConstructor
    @ToString
    public static class Attendee {
        private String username; //아이디
        private String name; //이름
    }

    //최근 메시지
    @Getter
    @ToString
    public static class LatestMessage {
        private String context;
        private long sendAt;

        @Builder
        public LatestMessage(String context, LocalDateTime sendAt) {
            this.context = context;
            this.sendAt = sendAt.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        }
    }
}
