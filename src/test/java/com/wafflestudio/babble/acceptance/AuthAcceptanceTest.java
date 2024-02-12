package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.acceptance.utils.TestClient;
import com.wafflestudio.babble.member.domain.MemberRepository;

@DisplayName("인증 관련 인수 테스트")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("새로운 유저가 로그인을 하면 회원이 생성되고 토큰을 발급한다.")
    @Test
    void signupWithLogin() {
        TestClient client = createTestClient();
        String accessToken = client.loginSuccess(KAKAO_AUTH_ID);
        assertThat(accessToken).isNotBlank();
        assertThat(memberRepository.findAll()).hasSize(1);
    }
}
