package com.wafflestudio.babble.chat.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.wafflestudio.babble.chat.application.dto.ChatsDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatsResponse {

    private List<ChatResponse> chats;

    public ChatsResponse(ChatsDto dto) {
        this.chats = dto.getValues().stream()
            .map(chatDto -> ChatResponse.of(chatDto, dto.getMyChatterId()))
            .collect(Collectors.toList());
    }
}
