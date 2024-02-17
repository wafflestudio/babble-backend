package com.wafflestudio.babble.location.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.location.domain.Location;

@Service
public class LocationService {

    private final double validationLimit;

    public LocationService(@Value("${location.validation-limit}") double validationLimit) {
        this.validationLimit = validationLimit;
    }

    public void validateClose(Location location, Location target) {
        if (location.calculateDistance(target) >= validationLimit) {
            throw new ForbiddenException("너무 멀리 있습니다. 가까이 가세요.");
        }
    }
}
