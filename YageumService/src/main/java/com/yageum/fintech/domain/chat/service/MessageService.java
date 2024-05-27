package com.yageum.fintech.domain.chat.service;


import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.domain.auth.service.MyUserDetailsService;
import com.yageum.fintech.domain.chat.dto.request.ChatSession;
import com.yageum.fintech.domain.chat.infrastructure.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 채팅방 소켓 연결과 관련된 서비스
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final ChatSessionRepository chatSessionRepository;

    /**
     * 사용자가 채팅방에 입장할 때 입장 내역을 Redis에 저장
     */
    @Transactional
    public void connectChatRoom(Long chatRoomNo, String username, Long houseId) {
        ChatSession chatSession = ChatSession.builder()
                .username(username)
                .chatroomNo(chatRoomNo)
                .houseId(houseId)
                .build();

        chatSessionRepository.save(chatSession);
    }

    /**
     * 사용자가 채팅방에서 나갈 때 입장 내역을 Redis에서 삭제
     */
    @Transactional
    public void disconnectChatRoom(Long chatRoomNo, String username) {
        ChatSession chatSession = chatSessionRepository.findByChatroomNoAndUsername(chatRoomNo, username)
                .orElseThrow(IllegalStateException::new);
        chatSessionRepository.delete(chatSession);
    }

    /**
     * 채팅방에 한 명만 연결되었는지 확인
     * 한 명만 연결된 경우 입장 메시지를 알림으로 보내는 데 사용됨
     *
     * @return 한 명만 연결된 경우 true, 그렇지 않으면 false
     */
    public boolean isConnected(Long chatRoomNo) {
        List<ChatSession> connectedList = chatSessionRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 1;
    }

    /**
     * 채팅방에 두 명이 모두 연결되었는지 확인
     *
     * @return 두 명이 연결된 경우 true, 그렇지 않으면 false
     */
    public boolean isAllConnected(Long chatRoomNo) {
        List<ChatSession> connectedList = chatSessionRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() == 2;
    }

    /**
     * 특정 매물에 해당하는 모든 채팅방을 삭제
     */
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
