package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.acceptance.utils.TestClient;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("새로운 유저가 로그인을 하면 회원이 생성되고 토큰을 발급한다.")
    @Test
    void signupWithLogin() {
        TestClient testClient = createTestClient();
        String accessToken = testClient.loginSuccess(KAKAO_AUTH_ID);
        assertThat(accessToken).isNotBlank();
    }
}
