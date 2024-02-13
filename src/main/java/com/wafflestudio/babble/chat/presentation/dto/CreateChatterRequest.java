package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.CreateChatterDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChatterRequest extends LocationRequest {

    // TODO: required = false로 바꾸고, 닉네임이 빈 경우 임의의 닉네임 생성
    @ApiModelProperty(required = true)
    private String nickname;

    public CreateChatterDto toDto(String authUserId, Long roomId) {
        return CreateChatterDto.of(authUserId, roomId, nickname, latitude, longitude);
    }

    public CreateChatterRequest(String nickname, Double latitude, Double longitude) {
        super(latitude, longitude);
        this.nickname = nickname;
    }
}

