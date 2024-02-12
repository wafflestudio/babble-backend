package com.wafflestudio.babble.chat.application.dto;

import java.util.List;

import com.wafflestudio.babble.chat.domain.ChatRoom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatRoomDetailDto {

    private final ChatRoomResponseDto room;
    private final Boolean isChatter;
    private final List<ChatDto> chats;

    public static ChatRoomDetailDto of(ChatRoom room, boolean isChatter, List<ChatDto> chats) {
        return new ChatRoomDetailDto(ChatRoomResponseDto.of(room), isChatter, chats);
    }
}
