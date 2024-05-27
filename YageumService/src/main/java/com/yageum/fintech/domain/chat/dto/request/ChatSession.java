package com.yageum.fintech.domain.chat.dto.request;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "chatSession")
public class ChatSession {

    @Id
    private String id;

    @Indexed
    private Long chatroomNo;

    @Indexed
    private Long houseId;

    @Indexed
    private String username;

    @Builder
    public ChatSession(Long chatroomNo, Long houseId, String username) {
        this.chatroomNo = chatroomNo;
        this.houseId = houseId;
        this.username = username;
    }
}

