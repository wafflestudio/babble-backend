package com.wafflestudio.babble.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.wafflestudio.babble.common.utils.RandomUtils;

public class RandomUtilsTest {

    @Nested
    @DisplayName("generateRandomAlphanumericString 메서드는")
    class GenerateRandomAlphanumericStringTest {

        @Test
        @DisplayName("영문자와 숫자로 구성된 문자열을 임의로 생성한다.")
        void regexTest() {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{1,6}$");

            String generatedString = RandomUtils.generateRandomAlphanumericString(6);
            Matcher matcher = pattern.matcher(generatedString);

            assertThat(matcher.matches()).isEqualTo(true);
        }

        @Test
        @DisplayName("호출될 때마다 중복되지 않는 새로운 문자열들을 임의로 생성한다.")
        void noDuplicatesTest() {
            HashSet<String> set = new HashSet<>();
            int count = 20;
            for (int i = 0; i < count; i++) {
                String generatedString = RandomUtils.generateRandomAlphanumericString(10);
                set.add(generatedString);
            }
            assertThat(set).hasSize(count);
        }

        @ParameterizedTest
        @DisplayName("지정된 길이만큼의 글자수로 구성된 문자열을 생성한다")
        @ValueSource(ints = {1, 5, 50})
        void lengthTest(int length) {
            String generatedString = RandomUtils.generateRandomAlphanumericString(length);
            assertThat(generatedString).hasSize(length);
        }
    }
}
