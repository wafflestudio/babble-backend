package com.wafflestudio.babble.chat.presentation.dto;

import com.wafflestudio.babble.chat.application.dto.ChatDto;
import com.wafflestudio.babble.chat.application.dto.CommonChatDto;

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
    private Long createdTimeInSec;
    private Boolean isMine;
    private ParentChatResponse parent;

    public static ChatResponse of(ChatDto dto, Long myChatterId) {
        CommonChatDto common = dto.getCommon();
        ParentChatResponse parentResponse = null;
        if (dto.hasParent()) {
            CommonChatDto parent = dto.getParent();
            parentResponse = new ParentChatResponse(parent.getId(), parent.getChatterId(), parent.getChatterNickname(), parent.getContent(),
                parent.getCreatedTimeInSec(), parent.isMyChat(myChatterId));
        }
        // TODO: 슬슬 빌더 패턴 적용하기
        return new ChatResponse(common.getId(), common.getChatterId(), common.getChatterNickname(), common.getContent(), common.getCreatedTimeInSec(),
            common.isMyChat(myChatterId), parentResponse);
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class ParentChatResponse {

        private Long id;
        private Long chatterId;
        private String chatterNickname;
        private String content;
        private Long createdTimeInSec;
        private Boolean isMine;
    }
}
