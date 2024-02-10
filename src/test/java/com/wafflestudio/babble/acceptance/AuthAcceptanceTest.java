package com.wafflestudio.babble.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.auth.presentation.dto.LoginResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("새로운 유저가 로그인을 하면 회원이 생성되고 토큰을 발급한다.")
    @Test
    void signupWithLogin() {
        Long kakaoAuthId = 123L;
        String code = "kakaoAuthCode";
        given(kakaoService.getAuthId(code)).willReturn(kakaoAuthId);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("code", code)
            .when()
            .post("/api/auth/login")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LoginResponse.class).getAccessToken()).isNotBlank();
    }
}
