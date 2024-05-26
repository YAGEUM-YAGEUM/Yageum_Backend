package com.yageum.fintech.domain.chat.infrastructure.repository;

import com.yageum.fintech.domain.chat.infrastructure.Chatting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Chatting, String> {
    List<Chatting> findByChatRoomNo(Long chatRoomNo);

    Page<Chatting> findByChatRoomNoOrderBySendDateDesc(Long chatRoomNo, Pageable pageable);

}
