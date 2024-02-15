package com.wafflestudio.babble.chat.application.dto;

import com.wafflestudio.babble.chat.domain.chatter.Chatter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatterDto {

    private final Long id;
    private final String nickname;

    public static ChatterDto of(Chatter chatter) {
        return new ChatterDto(chatter.getId(), chatter.getNickname());
    }
}
