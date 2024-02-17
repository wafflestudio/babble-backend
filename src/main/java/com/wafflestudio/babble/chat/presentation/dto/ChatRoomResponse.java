package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoomHashTag;
import com.wafflestudio.babble.location.domain.Location;

import lombok.Getter;

@Getter
public class ChatRoomResponse {

    private final Long id;
    private final String name;
    private final String hashTag;
    private final Double latitude;
    private final Double longitude;
    private final Integer distance;

    public ChatRoomResponse(Long id, String name, String hashTag, Double latitude, Double longitude, Integer distance) {
        this.id = id;
        this.name = name;
        this.hashTag = hashTag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public static ChatRoomResponse of(ChatRoomResponseDto dto) {
        Location location = dto.getLocation();
        ChatRoomHashTag hashTag = dto.getHashTag();
        // TODO: 빌더 패턴 적용하기
        return new ChatRoomResponse(dto.getId(), dto.getName(), hashTag.getDisplayName(), location.getLatitude(), location.getLongitude(),
            dto.getDistance());
    }
}
