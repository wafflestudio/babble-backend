package com.wafflestudio.babble.chat.application.dto;

import java.util.Objects;

import com.wafflestudio.babble.chat.domain.chat.Chat;
import com.wafflestudio.babble.chat.domain.chatter.Chatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class CommonChatDto {

    private final Long id;
    private final Long chatterId;
    private final String chatterNickname;
    private final String content;
    private final Long createdTimeInSec;

    public static CommonChatDto of(Chat chat) {
        Chatter chatter = chat.getChatter();
        return new CommonChatDto(chat.getId(), chatter.getId(), chatter.getNickname(), chat.getContent(), chat.getCreatedAtInSec());
    }

    public boolean isMyChat(Long myChatterId) {
        return Objects.equals(myChatterId, this.chatterId);
    }
}
