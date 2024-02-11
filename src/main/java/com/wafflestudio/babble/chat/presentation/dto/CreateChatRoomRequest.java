package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChatRoomRequest extends LocationRequest {

    private String nickname;
    private String roomName;
    private String hashTag;

    public CreateChatRoomDto toDto(String authUserId) {
        return CreateChatRoomDto.of(authUserId, nickname, roomName, hashTag, latitude, longitude);
    }

    public CreateChatRoomRequest(String nickname, String roomName, String hashTag, Double latitude, Double longitude) {
        super(latitude, longitude);
        this.nickname = nickname;
        this.roomName = roomName;
        this.hashTag = hashTag;
    }
}
