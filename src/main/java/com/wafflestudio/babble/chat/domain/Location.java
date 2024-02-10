package com.wafflestudio.babble.chat.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.wafflestudio.babble.common.exception.BadRequestException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public Location(Double latitude, Double longitude) {
        if (latitude < -90.0d || latitude > 90.0d ) {
            throw new BadRequestException("위도는 -90 ~ 90 사이의 값이어야 합니다.");
        }
        if (longitude < -180.0d || longitude > 180.0d ) {
            throw new BadRequestException("경도는 -180 ~ 180 사이의 값이어야 합니다.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
