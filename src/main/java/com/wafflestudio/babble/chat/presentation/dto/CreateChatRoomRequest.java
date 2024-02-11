package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChatRoomRequest extends LocationRequest {

    @ApiModelProperty(required = true)
    private String nickname;
    @ApiModelProperty(required = true)
    private String roomName;
    @ApiModelProperty(required = true, allowableValues = "LECTURE_ROOM, CAFETERIA, RESTAURANT, LIBRARY, DEPARTMENT_ROOM, CLUB_ACTIVITY_ROOM, CAFE")
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
