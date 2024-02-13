package com.wafflestudio.babble.chat.application.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatsDto {

    private final Long myChatterId;
    private final List<ChatDto> values;

    public static ChatsDto of(Long myChatterId, List<ChatDto> chats) {
        return new ChatsDto(myChatterId, chats);
    }

    public boolean isChatter() {
        return myChatterId != 0L;
    }
}
