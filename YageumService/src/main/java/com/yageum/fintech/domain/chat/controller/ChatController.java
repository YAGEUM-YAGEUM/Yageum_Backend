package com.yageum.fintech.domain.chat.controller;

import com.yageum.fintech.domain.chat.dto.request.ChatRoomRequestDto;
import com.yageum.fintech.domain.chat.service.ChatRoomService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import com.yageum.fintech.global.util.SecurityUtils;
import jakarta.security.auth.message.MessageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ResponseService responseService;

    //채팅방 생성
    @PostMapping("/chatroom")
    public CommonResult createChatRoom(@RequestBody @Valid final ChatRoomRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return responseService.getErrorResult(400, "검증 오류가 발생했습니다.");
        }

        // 채팅방을 만들어준다.
        chatRoomService.makeChatRoom(requestDto, SecurityUtils.getUsername());

        return responseService.getSuccessfulResult();
    }
    //채팅방 리스트 조회

    //채팅 내역 조회

   
}

