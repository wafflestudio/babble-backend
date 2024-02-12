package com.wafflestudio.babble.chat.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NearByChatRoomsResponse {

    private List<ChatRoomResponse> rooms;

    public NearByChatRoomsResponse(List<ChatRoomResponseDto> rooms) {
        this.rooms = rooms.stream()
            .map(ChatRoomResponse::of)
            .collect(Collectors.toList());
    }
}
