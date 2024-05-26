package com.yageum.fintech.domain.chat.service;

import com.yageum.fintech.domain.auth.jwt.JwtContextHolder;
import com.yageum.fintech.domain.auth.service.MyUserDetailsService;
import com.yageum.fintech.domain.chat.dto.request.ChatRoomRequestDto;
import com.yageum.fintech.domain.chat.infrastructure.ChatRoom;
import com.yageum.fintech.domain.chat.infrastructure.ChatRoomRepository;
import com.yageum.fintech.domain.house.service.HouseService;
import com.yageum.fintech.global.model.Exception.DealCompletedException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final HouseService houseService;
    private final MyUserDetailsService userDetailsService;

    @Transactional
    public ChatRoom makeChatRoom(ChatRoomRequestDto requestDto) {

        // 1. 매물 존재 여부 확인
        if (!houseService.existsById(requestDto.getHouseId())) {
            // 매물이 존재하지 않는 경우 에러 처리
            throw new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE);
        }

        // 2. 거래 상태 확인
        if (houseService.isHouseDealCompleted(requestDto.getHouseId())) {
            // 거래 완료 상태인 경우 에러 처리
            throw new DealCompletedException(ExceptionList.NON_EXISTENT_DEAL_COMPLETED);
        }

        // 3. 채팅방이 이미 존재하는지 확인
        Long houseId = requestDto.getHouseId();
        Long participantId = requestDto.getParticipantId();
        Long creatorId = getUserIdByUsername(JwtContextHolder.getUsername());

        // 3-1 채팅방이 이미 존재하는 경우 해당 채팅방 반환
        if (chatRoomRepository.existChatRoomByHouseIdAndMembers(houseId, creatorId, participantId)) {
            return chatRoomRepository.findChatRoomByHouseIdAndMembers(houseId, creatorId, participantId);
        }
        // 3-2 상대방이 이미 채팅방을 만든 경우 해당 채팅방 반환
        else if (chatRoomRepository.existChatRoomByHouseIdAndMembersReverse(houseId, creatorId, participantId)) {
            return chatRoomRepository.findChatRoomByHouseIdAndMembers(houseId, participantId, creatorId);
        }

        // 4. 채팅방 저장
        ChatRoom chatroom = ChatRoom.builder()
                .creatorId(creatorId)
                .participantId(participantId)
                .houseId(houseId)
                .regDate(LocalDateTime.now())
                .build();

        ChatRoom savedchatRoom = chatRoomRepository.save(chatroom);

//        // 채팅방 카운트 증가 : kafka
//        AggregationDto aggregationDto = AggregationDto
//                .builder()
//                .isIncrease("true")
//                .target(AggregationTarget.CHAT)
//                .saleNo(requestDto.getSaleNo())
//                .build();
//
//        aggregationSender.send(ConstantUtil.KAFKA_AGGREGATION, aggregationDto);
        return savedchatRoom;
    }

    // username을 통해 creatorId 조회
    private Long getUserIdByUsername(String username) {
        return userDetailsService.findUserIdByUsername(username);
    }
}
