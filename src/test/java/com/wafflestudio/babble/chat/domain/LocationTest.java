package com.wafflestudio.babble.chat.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.wafflestudio.babble.common.exception.BadRequestException;

public class LocationTest {

    @Nested
    @DisplayName("위도는 -90 ~ 90 사이의 값을 지녀야 한다.")
    class LatitudeTest {

        @ParameterizedTest
        @DisplayName("범위 이내")
        @ValueSource(doubles = {-90.0000000d, -89.9999999d, 89.9999999d, 90.0000000d})
        void latitudeInRange(double latitude) {
            assertThatNoException().isThrownBy(() -> new Location(latitude, 10.0d));
        }

        @ParameterizedTest
        @DisplayName("범위 초과")
        @ValueSource(doubles = {-90.0000001d, 90.0000001d})
        void latitudeOutOfRange(double latitude) {
            assertThatThrownBy(() -> new Location(latitude, 10.0d))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("위도는 -90 ~ 90 사이의 값이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("경도는 -180 ~ 180 사이의 값을 지녀야 한다.")
    class LongitudeTest {

        @ParameterizedTest
        @DisplayName("범위 이내")
        @ValueSource(doubles = {-180.0000000d, -179.9999999d, 179.9999999d, -180.0000000d})
        void longitudeInRange(double longitude) {
            assertThatNoException().isThrownBy(() -> new Location(10.0d, longitude));
        }

        @ParameterizedTest
        @DisplayName("범위 초과")
        @ValueSource(doubles = {-180.0000001d, 180.0000001d})
        void longitudeOutOfRange(double longitude) {
            assertThatThrownBy(() -> new Location(10.0d, longitude))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("경도는 -180 ~ 180 사이의 값이어야 합니다.");
        }
    }
}
