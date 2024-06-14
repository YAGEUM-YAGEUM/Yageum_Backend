package com.yageum.fintech.domain.chat.service;

import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.domain.auth.service.MyUserDetailsService;
import com.yageum.fintech.domain.chat.dto.request.ChatRoomRequestDto;
import com.yageum.fintech.domain.chat.dto.request.Message;
import com.yageum.fintech.domain.chat.dto.response.ChatResponseDto;
import com.yageum.fintech.domain.chat.dto.response.ChatRoomResponseDto;
import com.yageum.fintech.domain.chat.dto.response.ChattingHistoryResponseDto;
import com.yageum.fintech.domain.chat.infrastructure.ChatContentType;
import com.yageum.fintech.domain.chat.infrastructure.ChatRoom;
import com.yageum.fintech.domain.chat.infrastructure.Chatting;
import com.yageum.fintech.domain.chat.infrastructure.repository.ChatRoomRepository;
import com.yageum.fintech.domain.chat.infrastructure.repository.MessageRepository;
import com.yageum.fintech.domain.house.service.HouseService;
import com.yageum.fintech.global.model.Exception.DealCompletedException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.global.util.KafkaUtil;
import com.yageum.fintech.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 채팅방과 관련된 비즈니스 로직을 처리하는 클래스
 * - 채팅방 생성
 * - 채팅방 목록 조회
 * - 채팅 기록 조회
 * - 메시지 처리 (입장, 읽음 처리 등)
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MongoTemplate mongoTemplate;
    private final MessageSender messageSender;
    private final MessageService messageService;
    private final HouseService houseService;
    private final MyUserDetailsService userDetailsService;


    /** 새로운 채팅방 생성 */
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
        Long creatorId = getUserIdByUsername(SecurityUtils.getUsername());

        // 3-1 채팅방이 이미 존재하는 경우 해당 채팅방 반환
        if (chatRoomRepository.existsByHouseIdAndCreatorIdAndParticipantId(houseId, creatorId, participantId)) {
            return chatRoomRepository.findChatRoomByHouseIdAndMembers(houseId, creatorId, participantId);
        }
        // 3-2 상대방이 이미 채팅방을 만든 경우 해당 채팅방 반환
        else if (chatRoomRepository.existsByHouseIdAndParticipantIdAndCreatorId(houseId, creatorId, participantId)) {
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

        /**
         * 채팅방 집계를 위한 aggregation 도 kafka 로 추가 구현
         */

        return savedchatRoom;
    }

    /** 특정 회원이 속한 채팅방 목록을 조회힘 */
    @Transactional
    public List<ChatRoomResponseDto> getChatRoomList() {

        Long memberNo = getUserIdByUsername(SecurityUtils.getUsername());

        // 1. 내가 속한 채팅방 리스트 조회
        List<ChatRoomResponseDto> chatRoomList = chatRoomRepository.getChatRoomList(memberNo);

        //2. Participant 세팅
        chatRoomList
                .forEach(chatRoomDto -> {
                    // 2-1. 채팅방을 조회하려는 사람(memberNo)이 creator일 경우 => Attendee 에 participantId 정보 세팅
                    if (memberNo.equals(chatRoomDto.getCreatorId())) {
                        // ParticipantID 로부터 사용자 정보 조회
                        String username = userDetailsService.findUsernameById(chatRoomDto.getParticipantId());
                        String name = userDetailsService.findNameById(chatRoomDto.getParticipantId());

                        // Attendee에 참가한 멤버 설정
                        chatRoomDto.setAttendee(new ChatRoomResponseDto.Attendee(username, name));
                    }

                    // 2-2. 채팅방을 조회하려는 사람(memberNo)이 participant 경우 => Attendee 에 creator 세팅
                    if (memberNo.equals(chatRoomDto.getParticipantId())){
                        // CreatorIO 로부터 사용자 정보 조회
                        String username = userDetailsService.findUsernameById(chatRoomDto.getCreatorId());
                        String name = userDetailsService.findNameById(chatRoomDto.getCreatorId());

                        // Attendee에 참가한 멤버 설정
                        chatRoomDto.setAttendee(new ChatRoomResponseDto.Attendee(username, name));
                    }

                    // 3. unReadCount: 채팅방별로 읽지 않은 메시지 개수 설정
                    long unReadCount = countUnReadMessage(chatRoomDto.getChatRoomNo(), memberNo);
                    chatRoomDto.setUnReadCount(unReadCount);

                    // 4. LatestMessage: 채팅방별로 마지막 채팅 메시지와 전송 시간을 설정

                    // 4-1. 채팅방 번호(chatNo)에 해당하는 채팅 메시지를 최신 순으로 한 개(PageRequest.of(0, 1)) 조회
                    Page<Chatting> chatting =
                            messageRepository.findByChatRoomNoOrderBySendDateDesc(chatRoomDto.getChatRoomNo(),
                                    PageRequest.of(0, 1)); // 첫 번째 페이지의 한 개의 메시지 요청

                    // 4-2. 조회된 채팅 메시지가 있으면 첫번째 메시지 가져옴
                    if (chatting.hasContent()) {
                        Chatting chat = chatting.getContent().get(0);
                        ChatRoomResponseDto.LatestMessage latestMessage = ChatRoomResponseDto.LatestMessage.builder()
                                .context(chat.getContent())
                                .sendAt(chat.getSendDate())
                                .build();

                        // 4-3. 최신 메시지 정보를 채팅방 DTO에 설정
                        chatRoomDto.setLatestMessage(latestMessage);
                    }
                });

        return chatRoomList;
    }

    /** 특정 채팅방의 채팅 기록을 조회 */
    public ChattingHistoryResponseDto getChattingList(Long chatRoomNo) {

        Long memberNo = getUserIdByUsername(SecurityUtils.getUsername());
        updateCountAllZero(chatRoomNo, memberNo);

        String username = userDetailsService.findUsernameById(memberNo);
        List<ChatResponseDto> chattingList = messageRepository.findByChatRoomNo(chatRoomNo)
                .stream()
                .map(chat -> new ChatResponseDto(chat, memberNo)
                )
                .collect(Collectors.toList());

        return ChattingHistoryResponseDto.builder()
                .username(username)
                .chatList(chattingList)
                .build();
    }

    @Transactional
    public Message sendMessage(Message message, String accessToken) {

        String username = jwtTokenProvider.getUsername(accessToken);
        Long uid = getUserIdByUsername(username);
        String name = getNameByUsername(username);

        // 채팅방에 모든 유저가 참여 중인지 확인
        boolean isConnectedAll = messageService.isAllConnected(message.getChatRoomNo());

        // 1:1 채팅이므로 2명 접속 시 readCount 0, 한 명 접속 시 1
        Integer readCount = isConnectedAll ? 0 : 1;

        // message 객체에 보낸 시간, 보낸 사람 memberNo, 보낸 사람 이름을 설정
        message.setSendTimeAndSender(LocalDateTime.now(), uid, name, readCount);

        //메시지 전송
        messageSender.send(KafkaUtil.KAFKA_TOPIC, message);

        // Message 객체를 Chatting 객체로 변환
        // 메시지를 저장
        Chatting chatting = message.convertEntity();

        // 채팅 저장
        messageRepository.save(chatting);

        return message;
    }

    /** 참가자 입장 및 퇴장 메시지를 sub/chat/room/{chatRoomNo} 로 브로드캐스트 */
    public void broadcastEnterMessage(Long chatRoomNo, String name) {

        Message enterMessage = Message.builder()
                .contentType(ChatContentType.ENTER)
                .chatRoomNo(chatRoomNo)
                .content(name + " 님이 돌아오셨습니다.")
                .build();

        // 상대방에게 입장 메시지를 브로드캐스트
        messageSender.send(KafkaUtil.KAFKA_TOPIC, enterMessage);
    }
    public void broadcastExitMessage(Long chatRoomNo, String name) {
        Message exitMessage = Message.builder()
                .contentType(ChatContentType.EXIT)
                .chatRoomNo(chatRoomNo)
                .content(name + " 님이 채팅방을 나갔습니다..")
                .build();

        // 상대방에게 퇴장 메시지를 브로드캐스트
        messageSender.send(KafkaUtil.KAFKA_TOPIC, exitMessage);
    }

    /** 특정 채팅방에서 읽지 않은 메시지를 모두 읽음 처리 */
    public void updateCountAllZero(Long chatRoomNo, Long memberNo) {
        //MongoDB 업데이트 쿼리
        Update update = new Update().set("readCount", 0);

        //내가 보낸 메시지는 읽음 처리 필요 x
        Query query = new Query(Criteria.where("chatRoomNo").is(chatRoomNo)
                .and("senderId").ne(memberNo));

        mongoTemplate.updateMulti(query, update, Chatting.class);
    }

    /** 읽지 않은 메시지 개수 조회 */
    private long countUnReadMessage(Long chatRoomNo, Long memberNo) {
        //MongoDB 쿼리
        Query query = new Query(Criteria.where("chatRoomNo").is(chatRoomNo)
                .and("readCount").is(1)  // readCount가 1인 경우 읽지 않은 메시지
                .and("senderId").ne(memberNo));  // senderId가 현재 사용자가 아닌 경우

        return mongoTemplate.count(query, Chatting.class);
    }

    private Long getUserIdByUsername(String username) {
        return userDetailsService.findUserIdByUsername(username);
    }

    public String getNameByUsername(String username) {
        return userDetailsService.findNameByUsername(username);
    }

}
