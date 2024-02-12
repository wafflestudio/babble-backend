package com.wafflestudio.babble.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.TimeZone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimeConfigTest {

    @DisplayName("UTC 타임존을 기준으로 삼는다.")
    @Test
    void defaultTimeZone() {
        assertThat(TimeZone.getDefault().getID()).isEqualTo("UTC");
    }
}
