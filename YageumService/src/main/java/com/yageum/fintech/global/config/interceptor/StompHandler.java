package com.yageum.fintech.global.config.interceptor;

import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.domain.auth.service.MyUserDetailsService;
import com.yageum.fintech.domain.chat.service.ChatRoomService;
import com.yageum.fintech.domain.chat.service.MessageService;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.InvalidJwtTokenException;
import com.yageum.fintech.global.model.Exception.NullJwtTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

/** WebSocket을 통해 들어온 요청의 Stomp 헤더를 가로채어 JWT 토큰의 유효성을 검증하는 역할 **/

@Order(Ordered.HIGHEST_PRECEDENCE + 99) //스프링의 빈 순서를 지정하는 애노테이션
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final MyUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    /**
     * 웹소켓을 통해 들어온 요청이 처리되기 전 실행됨
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //StompHeaderAccessor : 메시지의 Stomp 헤더를 쉽게 접근하기 위한 유틸리티 클래스
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String accessToken = getAccessToken(accessor);
        if (accessToken == null) {
            throw new NullJwtTokenException(ExceptionList.NULL_JWT_TOKEN);
        }

        String username = verifyAccessToken(accessToken);
        log.info("StompAccessor = {}", accessor);

        // 인증 발급
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        accessor.setUser(authentication);

        handleMessage(accessor.getCommand(), accessor, username);
        return message;
    }

    /**
     * STOMP 명령어에 따라 다른 처리를 수행
     */
    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {
        switch (stompCommand) {
            case CONNECT:
                // 연결 설정만 처리
                log.info("User {} connected", username);
                break;
            case SUBSCRIBE:
                connectToChatRoom(accessor, username);
                break;
            case SEND:
                tokenProvider.getUsername(getAccessToken(accessor));
                break;
            case DISCONNECT:
                disconnectChatRoom(accessor, username);
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String verifyAccessToken(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new InvalidJwtTokenException(ExceptionList.INVALID_JWT);
        }
        return tokenProvider.getUsername(accessToken);

    }

    /**
     * 채팅방에 접속 처리 및 관련 작업 수행
     */
    private void connectToChatRoom(StompHeaderAccessor accessor, String username) {
        Long chatRoomNo = getChatRoomNo(accessor);
        Long houseId = getHouseId(accessor);

        String name = getNameByUsername(username);
        Long userId = getUserIdByUsername(username);

        // 1. 채팅방 입장 처리 -> Redis에 입장 내역 저장
        messageService.connectChatRoom(chatRoomNo, username, houseId);
        // 2. 읽지 않은 채팅을 전부 읽음 처리
        chatRoomService.updateCountAllZero(chatRoomNo, userId);
        // 3. 현재 채팅방에 접속 중인 인원이 있는지 확인
        boolean isConnected = messageService.isConnected(chatRoomNo);

        if (isConnected) {
            // 4. 입장 메시지 생성 및 브로드캐스트
            chatRoomService.broadcastEnterMessage(chatRoomNo, name);
        }

    }

    /**
     * 사용자 퇴장 처리를 담당하는 메소드
     */
    private void disconnectChatRoom(StompHeaderAccessor accessor, String username) {
        Long chatRoomNo = getChatRoomNo(accessor);
        String name = getNameByUsername(username);

        // 채팅방에 아직 누군가가 있으면 퇴장 메시지를 보냄
        if (messageService.isConnected(chatRoomNo)) {
            chatRoomService.broadcastExitMessage(chatRoomNo, name);
        }

        // 채팅방에서 퇴장 처리
        messageService.disconnectChatRoom(chatRoomNo, username);
    }

    /** STOMP 헤더에서 필요한 정보 추출 */
    private Long getChatRoomNo(StompHeaderAccessor accessor) {
        return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomNo")));
    }

    private Long getHouseId(StompHeaderAccessor accessor) {
        return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("houseId")));
    }

    /** 사용자 아이디로 필요한 정보 조회 */
    private String getNameByUsername(String username) {
        return userDetailsService.findNameByUsername(username);
    }

    private Long getUserIdByUsername(String username) {
        return userDetailsService.findUserIdByUsername(username);
    }
}
