package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yageum.fintech.domain.chat.dto.response.ChatRoomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.yageum.fintech.domain.chat.infrastructure.QChatRoom.chatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements CustomChatRoomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    //내가 참여 중인 채팅방 리스트 조회
    //엔티티와 다른 반환 타입 -> Projections
    @Override
    public List<ChatRoomResponseDto> getChatRoomList(@Param("memberNo") Long memberNo) {
        List<ChatRoomResponseDto> chatRoomList = jpaQueryFactory
                .select(Projections.constructor(ChatRoomResponseDto.class,
                        chatRoom.chatRoomNo,
                        chatRoom.creatorId,
                        chatRoom.participantId,
                        chatRoom.houseId,
                        chatRoom.regDate))
                .from(chatRoom)
                .where(chatRoom.creatorId.eq(memberNo).or(chatRoom.participantId.eq(memberNo)))
                .fetch();
        return chatRoomList;
    }
}
