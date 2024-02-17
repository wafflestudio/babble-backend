package com.wafflestudio.babble.location.domain;

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

    public static final int RADIUS_OF_EARTH_IN_METERS = 6371 * 1000;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public Location(Double latitude, Double longitude) {
        if (latitude < -90.0d || latitude > 90.0d) {
            throw new BadRequestException("위도는 -90 ~ 90 사이의 값이어야 합니다.");
        }
        if (longitude < -180.0d || longitude > 180.0d) {
            throw new BadRequestException("경도는 -180 ~ 180 사이의 값이어야 합니다.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine 공식</a>으로 계산한 두 지점 사이의 거리 (m 단위)
     */
    public int calculateDistance(Location location) {
        double latitudeDifference = degreeToRadius(location.latitude - this.latitude);
        double longitudeDifference = degreeToRadius(location.longitude - this.longitude);
        double value = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
            + Math.cos(degreeToRadius(this.latitude)) * Math.cos(degreeToRadius(location.latitude))
            * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        return (int) (RADIUS_OF_EARTH_IN_METERS * 2 * Math.atan2(Math.sqrt(value), Math.sqrt(1 - value)));
    }

    private double degreeToRadius(Double deg) {
        return deg * (Math.PI / 180);
    }

    @Override
    public String toString() {
        return String.format("[위도=%.1f, 경도=%.1f]", this.latitude, this.longitude);
    }
}
