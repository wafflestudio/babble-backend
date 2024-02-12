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
    private final Long myChatterId;
    private final Integer chatterCount;
    private final List<ChatDto> chats;

    public static ChatRoomDetailDto ofChatter(ChatRoom room, Long myChatterId, int chatterCount, List<ChatDto> chats) {
        return new ChatRoomDetailDto(ChatRoomResponseDto.of(room), true, myChatterId, chatterCount, chats);
    }

    public static ChatRoomDetailDto ofVisitor(ChatRoom room, int chatterCount, List<ChatDto> chats) {
        return new ChatRoomDetailDto(ChatRoomResponseDto.of(room), false, 0L, chatterCount, chats);
    }
}
