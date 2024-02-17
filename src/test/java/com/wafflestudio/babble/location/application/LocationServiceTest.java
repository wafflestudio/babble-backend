package com.wafflestudio.babble.location.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.location.domain.Location;

class LocationServiceTest {

    private final LocationService locationService = new LocationService(500);
    private final Location location = new Location(37.5, 127.0);

    @DisplayName("충분히 가까우면 예외가 발생하지 않는다")
    @Test
    void validateClose() {
        Location target = new Location(37.504, 127.0);
        assertThatNoException().isThrownBy(() -> locationService.validateClose(location, target));
    }

    @DisplayName("너무 멀면 에러가 발생한다")
    @Test
    void validateTooFar() {
        Location target = new Location(37.505, 127.0);
        assertThatThrownBy(() -> locationService.validateClose(location, target))
            .isInstanceOf(ForbiddenException.class);
    }
}
