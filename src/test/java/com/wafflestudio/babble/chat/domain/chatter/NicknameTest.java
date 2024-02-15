package com.wafflestudio.babble.chat.domain.chatter;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.wafflestudio.babble.common.exception.BadRequestException;

class NicknameTest {

    @ParameterizedTest
    @DisplayName("1~15글자 사이의 한글, 숫자, 영문자로 구성된 닉네임은 가능하다.")
    @ValueSource(strings = {"a", "가aB1", "열다섯글자ABCDE12345"})
    void success(String nickname) {
        assertThatNoException().isThrownBy(() -> new Nickname(nickname));
    }

    @Test
    @DisplayName("15글자를 초과한 닉네임은 예외를 발생시킨다.")
    void tooLong() {
        assertThatThrownBy(() -> new Nickname("열여섯글자ABCDE123456"))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("공백이 포함된 닉네임은 예외를 발생시킨다.")
    void hasBlank() {
        assertThatThrownBy(() -> new Nickname("정 진우"))
            .isInstanceOf(BadRequestException.class);
    }
}
