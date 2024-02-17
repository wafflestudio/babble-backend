package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.chatroom.ChatRoom;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoomHashTag;
import com.wafflestudio.babble.location.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatRoomResponseDto {

    private final Long id;
    private final String name;
    private final ChatRoomHashTag hashTag;
    private final Location location;
    private final Integer distance;

    public static ChatRoomResponseDto of(ChatRoom room, Location userLocation) {
        Location roomLocation = room.getLocation();
        int distance = userLocation.calculateDistance(roomLocation);
        return new ChatRoomResponseDto(room.getId(), room.getRoomName(), room.getHashTag(), roomLocation, distance);
    }
}
