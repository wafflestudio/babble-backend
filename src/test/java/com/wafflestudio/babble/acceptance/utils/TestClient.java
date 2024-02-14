package com.wafflestudio.babble.acceptance.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.wafflestudio.babble.auth.application.KakaoService;
import com.wafflestudio.babble.auth.presentation.dto.LoginResponse;
import com.wafflestudio.babble.chat.presentation.dto.ChatResponse;
import com.wafflestudio.babble.chat.presentation.dto.ChatRoomResponse;
import com.wafflestudio.babble.chat.presentation.dto.ChatsResponse;
import com.wafflestudio.babble.chat.presentation.dto.ChatterResponse;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRequest;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatterRequest;
import com.wafflestudio.babble.chat.presentation.dto.GetChatRoomResponse;
import com.wafflestudio.babble.chat.presentation.dto.NearByChatRoomsResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TestClient {

    private final KakaoService mockedKakaoService;

    private String accessToken;
    private final Map<Long, Long> latestChatIdMap = new HashMap<>();

    public TestClient(KakaoService mockedKakaoService) {
        this.mockedKakaoService = mockedKakaoService;
    }

    public String loginSuccess(Long kakaoAuthId) {
        String token = "token";
        given(mockedKakaoService.getUserId(token)).willReturn(kakaoAuthId);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .param("token", token)
            .when()
            .post("/api/auth/login")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        this.accessToken = response.as(LoginResponse.class).getAccessToken();
        return accessToken;
    }

    public Long createRoom(CreateChatRoomRequest body) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/chat/rooms")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = response.header(HttpHeaders.LOCATION);
        assertThat(location).isNotBlank();
        assertThat(location).startsWith("/api/chat/rooms/");
        return Long.valueOf(location.substring("/api/chat/rooms/".length()));
    }

    public List<ChatRoomResponse> getNearbyRooms(Double latitude, Double longitude) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .param("latitude", latitude)
            .param("longitude", longitude)
            .when()
            .get("/api/chat/rooms")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(NearByChatRoomsResponse.class).getRooms();
    }

    public GetChatRoomResponse getChatRoom(Long roomId, Double latitude, Double longitude) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .param("latitude", latitude)
            .param("longitude", longitude)
            .when()
            .get("/api/chat/rooms/" + roomId)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(GetChatRoomResponse.class);
    }

    public ChatterResponse createChatter(Long roomId, CreateChatterRequest body) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/chat/rooms/" + roomId + "/chatters")
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(ChatterResponse.class);
    }

    public ChatsResponse getChats(Long roomId, Double latitude, Double longitude) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .param("latestChatId", latestChatIdMap.getOrDefault(roomId, 0L))
            .param("latitude", latitude)
            .param("longitude", longitude)
            .when()
            .get("/api/chat/rooms/" + roomId + "/chats")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ChatsResponse chatsResponse = response.as(ChatsResponse.class);
        List<ChatResponse> chats = chatsResponse.getChats();
        if (!chats.isEmpty()) {
            latestChatIdMap.put(roomId, chats.get(0).getId());
        }
        return chatsResponse;
    }

    public ChatResponse createChatSuccess(Long roomId, CreateChatRequest body) {
        ExtractableResponse<Response> response = createChat(roomId, body);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(ChatResponse.class);
    }

    public ExtractableResponse<Response> createChat(Long roomId, CreateChatRequest body) {
        return RestAssured.given().log().all()
            .auth().oauth2(this.accessToken)
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/chat/rooms/" + roomId + "/chats")
            .then().log().all()
            .extract();
    }
}
