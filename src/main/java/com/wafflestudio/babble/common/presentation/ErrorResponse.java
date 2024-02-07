package com.wafflestudio.babble.common.presentation;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
