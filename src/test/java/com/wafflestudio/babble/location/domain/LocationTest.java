package com.wafflestudio.babble.location.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    @ParameterizedTest(name = "{0}~{1} : {2}m")
    @DisplayName("두 위치 사이의 거리를 m 단위로 계산한다")
    @MethodSource("calculateDistanceTestParameters")
    void calculateDistanceTest(Location location, Location target, double expected) {
        double actual = location.calculateDistance(target);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> calculateDistanceTestParameters() {
        return Stream.of(
            Arguments.of(new Location(10.0, 10.0), new Location(10.0, 10.0), 0),
            Arguments.of(new Location(10.0, 10.0), new Location(10.0, 10.1), 10950.562543608205),
            Arguments.of(new Location(10.0, 10.1), new Location(10.0, 10.0), 10950.562543608205),
            Arguments.of(new Location(10.0, 10.0), new Location(10.1, 10.0), 11119.492664455835),
            Arguments.of(new Location(10.1, 10.0), new Location(10.0, 10.0), 11119.492664455835),
            Arguments.of(new Location(10.0, 10.0), new Location(50.0, 50.0), 5763650.056682031),
            Arguments.of(new Location(37.5, 127.0), new Location(37.504, 127.0), 444.77970657798846),
            Arguments.of(new Location(37.5, 127.0), new Location(37.505, 127.0), 555.9746332230782)
        );
    }
}
