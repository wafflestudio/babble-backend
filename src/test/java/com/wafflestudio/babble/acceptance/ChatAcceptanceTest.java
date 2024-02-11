package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG;
import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static com.wafflestudio.babble.testutil.TestFixtures.LATITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.LONGITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wafflestudio.babble.acceptance.utils.AcceptanceTest;
import com.wafflestudio.babble.acceptance.utils.TestClient;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;

public class ChatAcceptanceTest extends AcceptanceTest {

    @DisplayName("유저는 현재 위치를 기반으로 채팅방을 생성하고, 본인이 방장이 된다.")
    @Test
    void signupWithLogin() {
        TestClient testClient = createTestClient();
        testClient.loginSuccess(KAKAO_AUTH_ID);
        String location = testClient.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        assertThat(location).isEqualTo("/api/chat/room/1");
        // TODO: 채팅방 조회 로직 추가
    }
}
