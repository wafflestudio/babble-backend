package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateChatterDto {

    private final String authUserId;
    private final Long roomId;
    private final String nickname;
    private final Location location;

    public static CreateChatterDto of(String authUserId, Long roomId, String nickname, Double latitude, Double longitude) {
        Location location = new Location(latitude, longitude);
        return new CreateChatterDto(authUserId, roomId, nickname, location);
    }
}
