package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.location.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GetChatRoomDto {

    private final String authUserId;
    private final Long roomId;
    private final Location location;

    public static GetChatRoomDto of(String authUserId, Long roomId, Double latitude, Double longitude) {
        Location location = new Location(latitude, longitude);
        return new GetChatRoomDto(authUserId, roomId, location);
    }
}
