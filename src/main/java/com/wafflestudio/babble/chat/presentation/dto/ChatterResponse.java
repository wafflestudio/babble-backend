package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.ChatterDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ChatterResponse {

    private Long id;
    private String nickname;

    public static ChatterResponse of(ChatterDto dto) {
        return new ChatterResponse(dto.getId(), dto.getNickname());
    }
}
