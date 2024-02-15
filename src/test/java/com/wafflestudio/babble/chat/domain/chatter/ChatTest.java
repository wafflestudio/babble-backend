package com.wafflestudio.babble.chat.domain.chatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.wafflestudio.babble.chat.domain.chat.Chat;
import com.wafflestudio.babble.common.exception.BadRequestException;

class ChatTest {

    @ParameterizedTest
    @DisplayName("채팅 내용에 대한 제약은 없다.")
    @ValueSource(strings = {"a", "가aB1", "특수문자포함!@#$%^&*()\""})
    void success(String content) {
        assertThatNoException().isThrownBy(() -> Chat.create(null, null, content));
    }

    @Test
    @DisplayName("최대 2000글자까지의 채팅은 생성할 수 있다")
    void allowedLength() {
        String content = "하".repeat(2000);
        assertThatNoException().isThrownBy(() -> Chat.create(null, null, content));
    }

    @Test
    @DisplayName("2000글자를 초과한 채팅은 생성할 수 없다")
    void tooLong() {
        String content = "하".repeat(2001);
        assertThatThrownBy(() -> Chat.create(null, null, content))
            .isInstanceOf(BadRequestException.class);
    }

    @ParameterizedTest
    @DisplayName("비어있거나 공백으로만 구성된 채팅은 생성할 수 없다")
    @ValueSource(strings = {"", " ", "   "})
    void isEmpty(String content) {
        assertThatThrownBy(() -> Chat.create(null, null, content))
            .isInstanceOf(BadRequestException.class);
    }
}
