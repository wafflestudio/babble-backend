package com.wafflestudio.babble.acceptance.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.wafflestudio.babble.auth.application.KakaoService;
import com.wafflestudio.babble.auth.presentation.dto.LoginResponse;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestClient {

    private final KakaoService mockedKakaoService;

    private String accessToken;

    public TestClient(KakaoService mockedKakaoService) {
        this.mockedKakaoService = mockedKakaoService;
    }

    public String loginSuccess(Long kakaoAuthId) {
        String code = "kakaoAuthCode";
        given(mockedKakaoService.getAuthId(code)).willReturn(kakaoAuthId);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("code", code)
            .when()
            .post("/api/auth/login")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        this.accessToken = response.as(LoginResponse.class).getAccessToken();
        return accessToken;
    }

    public String createRoom(CreateChatRoomRequest data) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .body(data)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/chat/room")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header(HttpHeaders.LOCATION);
        assertThat(location).isNotBlank();
        return location;
    }
}
