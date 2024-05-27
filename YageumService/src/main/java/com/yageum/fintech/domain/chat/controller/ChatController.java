package com.yageum.fintech.domain.chat.controller;

import com.yageum.fintech.domain.auth.jwt.JwtContextHolder;
import com.yageum.fintech.domain.chat.dto.request.ChatRoomRequestDto;
import com.yageum.fintech.domain.chat.dto.request.Message;
import com.yageum.fintech.domain.chat.dto.response.ChatRoomResponseDto;
import com.yageum.fintech.domain.chat.dto.response.ChattingHistoryResponseDto;
import com.yageum.fintech.domain.chat.service.ChatRoomService;
import com.yageum.fintech.domain.chat.service.MessageService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ResponseService responseService;

    //채팅방 생성
    @PostMapping("/chatroom")
    public CommonResult createChatRoom(@RequestBody @Valid final ChatRoomRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return responseService.getErrorResult(400, "검증 오류가 발생했습니다.");
        }

        // 채팅방을 만들어준다.
        chatRoomService.makeChatRoom(requestDto);

        return responseService.getSuccessfulResult();
    }

    //내가 속한 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public CommonResult chatRoomList() {
        List<ChatRoomResponseDto> chatList = chatRoomService.getChatRoomList(JwtContextHolder.getUid());
        return responseService.getListResult(chatList);
    }

    //채팅 내역 조회
    @GetMapping("/chatroom/{roomNo}")
    public CommonResult chattingList(@PathVariable("roomNo") Long roomNo) {
        ChattingHistoryResponseDto chattingList = chatRoomService.getChattingList(roomNo, JwtContextHolder.getUid());
        return responseService.getSingleResult(chattingList);
    }

    //채팅방 입장은 StompHandler(Subscribe)에서 처리

    // 채팅방 대화
    @MessageMapping("/chat/talk/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    public void talkUser(@DestinationVariable("roomId") Long roomId, @Payload Message message, @Header("Authorization") final String accessToken){
        String token = accessToken.substring(7);
        chatRoomService.sendMessage(message, token);
    }
    

    @MessageExceptionHandler
    @SendTo("/error")
    public String handleException(Exception e) {
        return "WebSocket 메시지 핸들러에서 예외가 발생했습니다: " + e;
    }

}

