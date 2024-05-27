package com.yageum.fintech.domain.chat.service;


import com.yageum.fintech.domain.chat.dto.request.ChatSession;
import com.yageum.fintech.domain.chat.infrastructure.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//채팅방 소켓 연결과 관련된 서비스

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final ChatSessionRepository chatSessionRepository;

    //채팅방 입장 처리 -> Redis에 입장 내역 저장
    @Transactional
    public void connectChatRoom(Long chatRoomNo, String username, Long houseId) {
        ChatSession chatSession = ChatSession.builder()
                .username(username)
                .chatroomNo(chatRoomNo)
                .houseId(houseId)
                .build();

        chatSessionRepository.save(chatSession);
    }

    //채팅방 disconnect -> 내역 삭제
    @Transactional
    public void disconnectChatRoom(Long chatRoomNo, String username) {
        ChatSession chatSession = chatSessionRepository.findByChatroomNoAndUsername(chatRoomNo, username)
                .orElseThrow(IllegalStateException::new);
        chatSessionRepository.delete(chatSession);
    }

    //채팅방 1명 연결됐는지 확인 -> 한명이면 입장 메시지 알림
    public boolean isConnected(Long chatRoomNo) {
        List<ChatSession> connectedList = chatSessionRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 1;
    }

    //채팅방 정원 2명 찼는지 확인
    public boolean isAllConnected(Long chatRoomNo) {
        List<ChatSession> connectedList = chatSessionRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 2;
    }

    //해당 매물에 해당하는 채팅방 삭제
    @Transactional
    public List<Long> delete(Long houseId) {
        List<ChatSession> chatRoomList = chatSessionRepository.findByHouseId(houseId);

        List<Long> deletedChatNos = chatRoomList.stream().map(chatRoom -> {
            Long chatRoomNo = chatRoom.getChatroomNo();
            chatSessionRepository.delete(chatRoom);
            return chatRoomNo;
        }).collect(Collectors.toList());

        return deletedChatNos;
    }
}
