package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.chatroom.ChatRoomHashTag;
import com.wafflestudio.babble.location.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateChatRoomDto {

    private final String authUserId;
    private final String nickname;
    private final String roomName;
    private final ChatRoomHashTag hashTag;
    private final Location location;

    public static CreateChatRoomDto of(String authUserId, String nickname, String roomName, String hashTag, Double latitude, Double longitude) {
        ChatRoomHashTag chatRoomHashTag = ChatRoomHashTag.valueOf(hashTag);
        Location location = new Location(latitude, longitude);
        return new CreateChatRoomDto(authUserId, nickname, roomName, chatRoomHashTag, location);
    }
}
