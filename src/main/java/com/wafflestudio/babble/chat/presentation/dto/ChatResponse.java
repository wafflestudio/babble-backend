package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.ChatDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ChatResponse {

    private Long id;
    private Long chatterId;
    private String chatterNickname;
    private String content;
    private Boolean isMine;
    private Long createdTimeInSec;

    public static ChatResponse of(ChatDto dto, Long myChatterId) {
        Boolean isMine = dto.isMyChat(myChatterId);
        return new ChatResponse(dto.getId(), dto.getChatterId(), dto.getChatterNickname(), dto.getContent(), isMine, dto.getCreatedTimeInSec());
    }
}
