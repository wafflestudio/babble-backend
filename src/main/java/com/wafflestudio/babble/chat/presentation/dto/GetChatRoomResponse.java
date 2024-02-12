package com.wafflestudio.babble.chat.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.wafflestudio.babble.chat.application.dto.ChatRoomDetailDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChatRoomResponse {

    private ChatRoomResponse room;
    private Boolean isChatter;
    private List<ChatResponse> chats;

    public static GetChatRoomResponse of(ChatRoomDetailDto dto) {
        List<ChatResponse> chats = dto.getChats().stream()
            .map(ChatResponse::of)
            .collect(Collectors.toList());
        return new GetChatRoomResponse(ChatRoomResponse.of(dto.getRoom()), dto.getIsChatter(), chats);
    }
}
