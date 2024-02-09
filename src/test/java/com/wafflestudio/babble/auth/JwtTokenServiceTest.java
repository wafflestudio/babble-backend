package com.wafflestudio.babble.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wafflestudio.babble.auth.application.JwtTokenService;
import com.wafflestudio.babble.common.exception.UnauthenticatedException;

@DisplayName("JwtTokenService 클래스에서")
class JwtTokenServiceTest {

    public static final String SECRET_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    public static final long VALIDITY_IN_MS = 100000L;
    private final JwtTokenService jwtTokenService = new JwtTokenService(SECRET_KEY, VALIDITY_IN_MS);
    private final JwtTokenService wrongTokenService = new JwtTokenService("wrong" + SECRET_KEY, VALIDITY_IN_MS);

    @Nested
    @DisplayName("올바른 키로 토큰을 발급받고")
    class SecretKeyTest {

        private long now = 1600000000;

        @Test
        @DisplayName("동일한 키를 사용하면 해당 토큰의 payload를 추출할 수 있다.")
        void success() {
            String token = jwtTokenService.createToken("abc123", now);
            String payload = jwtTokenService.getValidatedPayload(token, now);

            assertThat(payload).isEqualTo("abc123");
        }

        @Test
        @DisplayName("다른 키를 사용하면 토큰 검증 과정에서 예외가 발생한다.")
        void fail() {
            String fakeToken = wrongTokenService.createToken("abc123", now);
            assertThatThrownBy(() -> jwtTokenService.getValidatedPayload(fakeToken, now))
                .isInstanceOf(UnauthenticatedException.class);
        }
    }


    @Nested
    @DisplayName("과거의 발급받은 토큰의 경우")
    class ValidityTest {

        public long past = 1600000000;
        public String token = jwtTokenService.createToken("abc123", past);

        @Test
        @DisplayName("만료기간이 지나지 않았으면 payload를 문제가 발생하지 않는다.")
        void success_onTime() {
            long now = past + VALIDITY_IN_MS;

            String payload = jwtTokenService.getValidatedPayload(token, now);

            assertThat(payload).isEqualTo("abc123");
        }

        @Test
        @DisplayName("만료기간이 지난 경우 예외가 발생한다.")
        void fail_expiredToken() {
            long now = past + VALIDITY_IN_MS + 1;

            assertThatThrownBy(() -> jwtTokenService.getValidatedPayload(token, now))
                .isInstanceOf(UnauthenticatedException.class);
        }
    }
}
