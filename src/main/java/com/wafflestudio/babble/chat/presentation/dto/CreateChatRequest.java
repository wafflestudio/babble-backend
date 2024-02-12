package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.CreateChatDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateChatRequest extends LocationRequest {

    @ApiModelProperty(required = true, value = "채팅 내용")
    private String content;

    public CreateChatDto toDto(String authUserId, Long roomId) {
        return CreateChatDto.of(authUserId, roomId, content, latitude, longitude);
    }

    public CreateChatRequest(String content, Double latitude, Double longitude) {
        super(latitude, longitude);
        this.content = content;
    }
}
