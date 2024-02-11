package com.wafflestudio.babble.chat.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wafflestudio.babble.chat.application.ChatService;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.common.presentation.AuthUserId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@AuthUserId String authId,
                                           @RequestBody CreateChatRoomRequest requestBody) {
        Long createdId = chatService.createChatRoom(requestBody.toDto(authId));
        URI location = URI.create("/api/chat/room/" + createdId);
        return ResponseEntity.created(location).build();
    }
}
