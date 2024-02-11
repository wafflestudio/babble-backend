package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.ChatRoom;
import com.wafflestudio.babble.chat.domain.ChatRoomHashTag;
import com.wafflestudio.babble.chat.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatRoomResponseDto {

    private final Long id;
    private final String name;
    private final ChatRoomHashTag hashTag;
    private final Location location;

    public static ChatRoomResponseDto of(ChatRoom room) {
        return new ChatRoomResponseDto(room.getId(), room.getRoomName(), room.getHashTag(), room.getLocation());
    }
}
