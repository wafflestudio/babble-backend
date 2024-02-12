package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateChatDto {

    private final String authUserId;
    private final Long roomId;
    private final String content;
    private final Location location;

    public static CreateChatDto of(String authUserId, Long roomId, String content, Double latitude, Double longitude) {
        Location location = new Location(latitude, longitude);
        return new CreateChatDto(authUserId, roomId, content, location);
    }
}
