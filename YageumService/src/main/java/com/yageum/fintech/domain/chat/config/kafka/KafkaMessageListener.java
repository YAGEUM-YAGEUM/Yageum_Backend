package com.yageum.fintech.domain.chat.config.kafka;

import com.example.chatService.domain.Message;
import com.example.chatService.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = {"studioi-chat", "studioi-chatroom"}, groupId = "group-id")
    public void listen(Message message) {
        // Kafka로부터 메시지 수신
        messageService.saveChatMessage(message);

        // WebSocket을 통해 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend("/subscribe/" + message.getChatNo(), message);
    }
}
