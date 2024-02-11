package com.wafflestudio.babble.chat.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocationRequest {

    @ApiModelProperty(required = true, value = "위도 - 얼마나 북쪽인가 (-90.0 ~ 90.0)")
    protected Double latitude;
    @ApiModelProperty(required = true, value = "경도 : 얼마나 동쪽인가 (-180.0 ~ 180.0)")
    protected Double longitude;
}
