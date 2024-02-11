package com.wafflestudio.babble.chat.presentation;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wafflestudio.babble.chat.application.ChatService;
import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.presentation.dto.NearByChatRoomsResponse;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.common.presentation.AuthUserId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController implements SwaggerChatController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    public ResponseEntity<NearByChatRoomsResponse> getNearbyRooms(@RequestParam Double latitude,
                                                                  @RequestParam Double longitude) {
        List<ChatRoomResponseDto> rooms = chatService.getNearbyRooms(latitude, longitude);
        return ResponseEntity.ok().body(new NearByChatRoomsResponse(rooms));
    }

    @PostMapping("/rooms")
    public ResponseEntity<Void> createRoom(@AuthUserId String authId,
                                           @RequestBody CreateChatRoomRequest requestBody) {
        Long createdId = chatService.createChatRoom(requestBody.toDto(authId));
        URI location = URI.create("/api/chat/rooms/" + createdId); // TODO: 단일 조회 API 추가
        return ResponseEntity.created(location).build();
    }
}
