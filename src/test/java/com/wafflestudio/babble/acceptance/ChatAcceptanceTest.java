package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.chat.domain.ChatRoomHashTag.LECTURE_ROOM;
import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG;
import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static com.wafflestudio.babble.testutil.TestFixtures.LATITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.LONGITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.acceptance.utils.TestClient;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.chat.presentation.dto.NearByChatRoomsResponse.ChatRoomResponse;

public class ChatAcceptanceTest extends AcceptanceTest {

    @DisplayName("유저는 현재 위치를 기반으로 채팅방을 생성하고, 본인이 방장이 된다.")
    @Test
    void createRoom() {
        TestClient client = createTestClient();
        client.loginSuccess(KAKAO_AUTH_ID);
        String location = client.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        assertThat(location).isEqualTo("/api/chat/rooms/1");
        // TODO: 채팅방 단일 로직 추가
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
