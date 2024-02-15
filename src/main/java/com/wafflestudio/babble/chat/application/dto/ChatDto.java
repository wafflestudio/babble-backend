package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.Chat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ChatDto {

    private final CommonChatDto common;
    private final CommonChatDto parent;

    public static ChatDto of(Chat chat) {
        if (chat.hasParent()) {
            return new ChatDto(CommonChatDto.of(chat), CommonChatDto.of(chat.getParentChat()));
        }
        return new ChatDto(CommonChatDto.of(chat), null);
    }

    public boolean hasParent() {
        return parent != null && parent.getId() > 0L;
    }

    public Long getChatterId() {
        return common.getChatterId();
    }
}
