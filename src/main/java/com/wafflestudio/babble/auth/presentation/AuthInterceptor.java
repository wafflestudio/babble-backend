package com.wafflestudio.babble.auth.presentation;

import static org.hibernate.validator.internal.metadata.core.ConstraintHelper.PAYLOAD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wafflestudio.babble.auth.application.JwtTokenService;
import com.wafflestudio.babble.common.exception.UnauthenticatedException;
import com.wafflestudio.babble.common.utils.TimeUtils;

public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;

    public AuthInterceptor(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        String token = AuthorizationExtractor.extractBearerToken(request);
        try {
            String payload = jwtTokenService.getValidatedPayload(token, TimeUtils.getCurrentUnixTimestamp());
            request.setAttribute(PAYLOAD, payload);
            return true;
        } catch (UnauthenticatedException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
