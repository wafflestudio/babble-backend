package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.chat.domain.ChatRoomHashTag.LECTURE_ROOM;
import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG;
import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG_DISPLAYNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static com.wafflestudio.babble.testutil.TestFixtures.LATITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.LONGITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.acceptance.utils.TestClient;
import com.wafflestudio.babble.chat.presentation.dto.ChatResponse;
import com.wafflestudio.babble.chat.presentation.dto.ChatRoomResponse;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRequest;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.chat.presentation.dto.GetChatRoomResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("채팅 관련 인수 테스트")
public class ChatAcceptanceTest extends AcceptanceTest {

    @DisplayName("현재 위치를 기반으로 유저가 채팅방을 생성하고")
    @Nested
    class CreateRoom {

        TestClient manager;
        Long roomId;

        @BeforeEach
        void setup() {
            manager = createTestClient();
            manager.loginSuccess(KAKAO_AUTH_ID);
            String location = manager.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
            assertThat(location).startsWith("/api/chat/rooms/");
            roomId = Long.valueOf(location.substring("/api/chat/rooms/".length()));
        }

        @DisplayName("방장이 된 유저가 해당 방에 들어가면 이미 참여 중인 것으로 뜨며, 채팅을 만들 수 있다")
        @Test
        void createAndGetChatRoom() {
            GetChatRoomResponse response = manager.getChatRoom(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isTrue();
            checkRoom(response.getRoom());
            assertThat(response.getChats()).hasSize(0);

            ChatResponse chat1 = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
            ChatResponse chat2 = manager.createChatSuccess(roomId, new CreateChatRequest("달이 아름답네요!", LATITUDE, LONGITUDE));
            ChatResponse chat3 = manager.createChatSuccess(roomId, new CreateChatRequest("그럼 이만!", LATITUDE, LONGITUDE));

            response = manager.getChatRoom(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isTrue();
            checkRoom(response.getRoom());
            assertThat(response.getChatterCount()).isEqualTo(1);
            assertThat(response.getChats()).hasSize(3);
            assertThat(response.getChats().get(0)).isEqualTo(chat3);
            assertThat(response.getChats().get(1)).isEqualTo(chat2);
            assertThat(response.getChats().get(2)).isEqualTo(chat1);
        }

        @DisplayName("다른 유저는 해당 채팅방에 들어가면 아직 참여 중이지 않다고 뜨며 채팅을 만들 수도 없다")
        @Test
        void visitor() {
            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);

            GetChatRoomResponse response = anotherMember.getChatRoom(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isFalse();
            checkRoom(response.getRoom());
            assertThat(response.getChatterCount()).isEqualTo(1);
            assertThat(response.getChats()).hasSize(0);

            ExtractableResponse<Response> errorResponse = anotherMember.createChat(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
            assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        }

        @DisplayName("채팅을 만들면 어떤 것이 본인의 채팅인지 구분할 수 있다")
        @Test
        void isMine() {
            ChatResponse chat = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);

            GetChatRoomResponse response = manager.getChatRoom(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getChats()).hasSize(1);
            assertThat(response.getChats().get(0).getId()).isEqualTo(chat.getId());
            assertThat(response.getChats().get(0).getIsMine()).isTrue();

            response = anotherMember.getChatRoom(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getChats()).hasSize(1);
            assertThat(response.getChats().get(0).getId()).isEqualTo(chat.getId());
            assertThat(response.getChats().get(0).getIsMine()).isFalse();
        }

        private void checkRoom(ChatRoomResponse roomResponse) {
            assertThat(roomResponse.getName()).isEqualTo(ROOM_NAME);
            assertThat(roomResponse.getHashTag()).isEqualTo(HASHTAG_DISPLAYNAME);
            assertThat(roomResponse.getLatitude()).isEqualTo(LATITUDE);
            assertThat(roomResponse.getLongitude()).isEqualTo(LONGITUDE);
        }
    }

    @DisplayName("유저는 현재 위치를 기반으로 채팅방 목록을 조회할 수 있다.")
    @Test
    void getNearbyRooms() {
        TestClient client = createTestClient();
        client.loginSuccess(KAKAO_AUTH_ID);
        for (int i = 0; i < 3; i++) {
            client.createRoom(new CreateChatRoomRequest("닉네임" + i, "방" + i, LECTURE_ROOM.name(), LATITUDE, LONGITUDE));
        }

        List<ChatRoomResponse> rooms = client.getNearbyRooms(LATITUDE, LONGITUDE);
        // TODO: 위치 기반 필터링 테스트 추가

        assertThat(rooms.size()).isEqualTo(3);
        for (int i = 0; i < 3; i++) {
            ChatRoomResponse room = rooms.get(i);
            assertThat(room.getId()).isEqualTo(i + 1);
            assertThat(room.getName()).isEqualTo("방" + i);
            assertThat(room.getHashTag()).isEqualTo(LECTURE_ROOM.getDisplayName());
            assertThat(room.getLongitude()).isEqualTo(LATITUDE);
            assertThat(room.getLatitude()).isEqualTo(LONGITUDE);
        }
    }
}
