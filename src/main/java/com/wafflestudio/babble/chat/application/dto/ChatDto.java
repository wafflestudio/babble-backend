package com.wafflestudio.babble.chat.application.dto;

import java.util.Objects;

import com.wafflestudio.babble.chat.domain.Chat;
import com.wafflestudio.babble.chat.domain.Chatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ChatDto {

    private final Long id;
    private final Long chatterId;
    private final String chatterNickname;
    private final String content;
    private final Long createdTimeInSec;

    public static ChatDto of(Chat chat, Chatter chatter) {
        return new ChatDto(chat.getId(), chatter.getId(), chatter.getNickname(), chat.getContent(), chat.getCreatedAtInSec());
    }

    public boolean isMyChat(Long myChatterId) {
        return Objects.equals(myChatterId, this.chatterId);
    }
}
