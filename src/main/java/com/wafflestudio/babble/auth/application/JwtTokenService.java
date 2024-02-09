package com.wafflestudio.babble.auth.application;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wafflestudio.babble.common.exception.UnauthenticatedException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

    private final SecretKey secretKey;
    private final long validityInMs;

    public JwtTokenService(@Value("${auth.jwt.secret-key}") final String secretKey,
                           @Value("${auth.jwt.expire-length}") final Long validityInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMs = validityInMs;
    }

    public String createToken(String payload, long now) {
        return Jwts.builder()
            .setClaims(Jwts.claims().setSubject(payload))
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + validityInMs))
            .signWith(secretKey)
            .compact();
    }

    public String getValidatedPayload(String token, long now) {
        try {
            return Jwts.parserBuilder()
                .setClock(() -> new Date(now))
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthenticatedException("토큰이 유효하지 않습니다.");
        }
    }
}
