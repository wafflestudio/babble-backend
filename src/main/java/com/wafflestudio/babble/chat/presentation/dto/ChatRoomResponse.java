package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.domain.ChatRoomHashTag;
import com.wafflestudio.babble.chat.domain.Location;

import lombok.Getter;
@Getter
public class ChatRoomResponse extends LocationResponse {

    private final Long id;
    private final String name;
    private final String hashTag;

    public ChatRoomResponse(Long id, String name, String hashTag, Double latitude, Double longitude) {
        super(latitude, longitude);
        this.id = id;
        this.name = name;
        this.hashTag = hashTag;
    }

    public static ChatRoomResponse of(ChatRoomResponseDto dto) {
        Location location = dto.getLocation();
        ChatRoomHashTag hashTag = dto.getHashTag();
        return new ChatRoomResponse(dto.getId(), dto.getName(), hashTag.getDisplayName(), location.getLatitude(), location.getLongitude());
    }
}
