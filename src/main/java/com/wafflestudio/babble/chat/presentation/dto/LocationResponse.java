package com.wafflestudio.babble.chat.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocationResponse {

    protected final Double latitude;
    protected final Double longitude;
}
