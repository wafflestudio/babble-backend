package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.location.domain.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GetLatestChatsDto {

    private final String authUserId;
    private final Long roomId;
    private final Long latestChatId;
    private final Location location;

    public static GetLatestChatsDto of(String authUserId, Long roomId, Long latestChatId, Double latitude, Double longitude) {
        Location location = new Location(latitude, longitude);
        return new GetLatestChatsDto(authUserId, roomId, latestChatId, location);
    }

    public Long getLatestChatId() {
        if (latestChatId == null) {
            return 0L;
        }
        return latestChatId;
    }
}
