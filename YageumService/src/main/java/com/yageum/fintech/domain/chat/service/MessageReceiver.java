package com.yageum.fintech.domain.chat.service;

import com.yageum.fintech.domain.chat.dto.request.Message;
import com.yageum.fintech.global.util.KafkaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiver {

    private final SimpMessagingTemplate messagingTemplate; //특정 Broker로 메세지를 전달

    @KafkaListener(topics = KafkaUtil.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(Message message) {

        log.info("전송 위치 = /sub/chat/room/"+ message.getChatRoomNo());
        log.info("채팅 방으로 메시지 전송 = {}", message);

        // 메시지객체 내부의 채팅방번호를 참조하여, 해당 채팅방 구독자에게 메시지를 발송한다.
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoomNo(), message);

    }
}
