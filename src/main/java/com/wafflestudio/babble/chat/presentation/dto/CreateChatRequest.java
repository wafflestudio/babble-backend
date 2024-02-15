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

    @ApiModelProperty(value = "답장일 경우, 답장 대상의 채팅")
    private Long parentChatId;

    public CreateChatDto toDto(String authUserId, Long roomId) {
        if (parentChatId == null || parentChatId <= 0L) {
            return CreateChatDto.of(authUserId, roomId, content, 0L, latitude, longitude);
        }
        return CreateChatDto.of(authUserId, roomId, content, parentChatId, latitude, longitude);
    }

    public CreateChatRequest(String content, Long parentChatId, Double latitude, Double longitude) {
        super(latitude, longitude);
        this.content = content;
        this.parentChatId = parentChatId;
    }

    public CreateChatRequest(String content, Double latitude, Double longitude) {
        this(content, null, latitude, longitude);
    }
}
