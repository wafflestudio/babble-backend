package com.wafflestudio.babble.acceptance;

import static com.wafflestudio.babble.chat.domain.chatroom.ChatRoomHashTag.LECTURE_ROOM;
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
import com.wafflestudio.babble.chat.presentation.dto.ChatterResponse;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRequest;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.chat.presentation.dto.CreateChatterRequest;
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
            roomId = manager.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        }

        @DisplayName("방장이 된 유저가 해당 방에 들어가면 이미 참여 중인 것으로 뜨며, 채팅을 만들 수 있다")
        @Test
        void createAndGetChatRoom() {
            GetChatRoomResponse response = manager.getChatRoomSuccess(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isTrue();
            checkRoom(response.getRoom());
            assertThat(response.getChats()).hasSize(0);

            ChatResponse chat1 = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
            ChatResponse chat2 = manager.createChatSuccess(roomId, new CreateChatRequest("달이 아름답네요!", LATITUDE, LONGITUDE));
            ChatResponse chat3 = manager.createChatSuccess(roomId, new CreateChatRequest("그럼 이만!", LATITUDE, LONGITUDE));

            response = manager.getChatRoomSuccess(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isTrue();
            checkRoom(response.getRoom());
            assertThat(response.getChatterCount()).isEqualTo(1);
            assertThat(response.getChats()).hasSize(3);
            assertThat(response.getChats().get(0)).isEqualTo(chat3);
            assertThat(response.getChats().get(1)).isEqualTo(chat2);
            assertThat(response.getChats().get(2)).isEqualTo(chat1);
        }

        @DisplayName("다른 유저가 해당 채팅방을 조회하면 아직 참여 중이지 않다고 뜨며 채팅을 만들 수도 없다")
        @Test
        void visitor() {
            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);

            GetChatRoomResponse response = anotherMember.getChatRoomSuccess(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getIsChatter()).isFalse();
            checkRoom(response.getRoom());
            assertThat(response.getChatterCount()).isEqualTo(1);
            assertThat(response.getChats()).hasSize(0);

            ExtractableResponse<Response> errorResponse = anotherMember.createChat(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
            assertThat(errorResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        }

        @DisplayName("다른 유저가 채팅방에 참여하면 채팅을 만들 수 있다")
        @Test
        void visitorEnter() {
            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);

            ChatterResponse response = anotherMember.createChatterSuccess(roomId, new CreateChatterRequest("검은고양이", LATITUDE, LONGITUDE));
            assertThat(response.getNickname()).isEqualTo("검은고양이");
            anotherMember.createChatSuccess(roomId, new CreateChatRequest("안녕하세요!", LATITUDE, LONGITUDE));
        }

        @DisplayName("채팅을 만들면 어떤 것이 본인의 채팅인지 구분할 수 있다")
        @Test
        void isMine() {
            ChatResponse chat1 = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요 1", LATITUDE, LONGITUDE));
            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);
            anotherMember.createChatterSuccess(roomId, new CreateChatterRequest("검은고양이", LATITUDE, LONGITUDE));
            ChatResponse chat2 = anotherMember.createChatSuccess(roomId, new CreateChatRequest("안녕하세요 2", LATITUDE, LONGITUDE));

            GetChatRoomResponse response = manager.getChatRoomSuccess(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getChats()).hasSize(2);
            assertThat(response.getChats().get(0).getId()).isEqualTo(chat2.getId());
            assertThat(response.getChats().get(0).getIsMine()).isFalse();
            assertThat(response.getChats().get(1).getId()).isEqualTo(chat1.getId());
            assertThat(response.getChats().get(1).getIsMine()).isTrue();

            response = anotherMember.getChatRoomSuccess(roomId, LATITUDE, LONGITUDE);
            assertThat(response.getRoom().getId()).isEqualTo(roomId);
            assertThat(response.getChats()).hasSize(2);
            assertThat(response.getChats().get(0).getId()).isEqualTo(chat2.getId());
            assertThat(response.getChats().get(0).getIsMine()).isTrue();
            assertThat(response.getChats().get(1).getId()).isEqualTo(chat1.getId());
            assertThat(response.getChats().get(1).getIsMine()).isFalse();
        }

        @DisplayName("채팅방 생성 위치로부터 너무 멀리 떨어지면 해당 채팅방과 상호작용할 수 없다")
        @Test
        void locationValidation() {
            double tooFarLatitude = LATITUDE + 1;
            ExtractableResponse<Response> response = manager.createChat(roomId, new CreateChatRequest("안녕하세요", tooFarLatitude, LONGITUDE));
            assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

            response = manager.getChatRoom(roomId, tooFarLatitude, LONGITUDE);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

            response = manager.getChats(roomId, tooFarLatitude, LONGITUDE);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

            TestClient anotherMember = createTestClient();
            anotherMember.loginSuccess(KAKAO_AUTH_ID + 1L);
            response = anotherMember.createChatter(roomId, new CreateChatterRequest("검은고양이", tooFarLatitude, LONGITUDE));
            assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        }

        private void checkRoom(ChatRoomResponse roomResponse) {
            assertThat(roomResponse.getName()).isEqualTo(ROOM_NAME);
            assertThat(roomResponse.getHashTag()).isEqualTo(HASHTAG_DISPLAYNAME);
            assertThat(roomResponse.getLatitude()).isEqualTo(LATITUDE);
            assertThat(roomResponse.getLongitude()).isEqualTo(LONGITUDE);
        }
    }

    @DisplayName("각 유저는 가장 최근에 읽은 채팅 이후에 생성된 채팅들을 조회할 수 있다")
    @Test
    void getChats() {
        TestClient manager = createTestClient();
        manager.loginSuccess(KAKAO_AUTH_ID);
        Long roomId = manager.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));

        TestClient chatter = createTestClient();
        chatter.loginSuccess(KAKAO_AUTH_ID + 1L);
        chatter.createChatterSuccess(roomId, new CreateChatterRequest("검은고양이", LATITUDE, LONGITUDE));

        ChatResponse chat1 = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요 1", LATITUDE, LONGITUDE));
        ChatResponse chat2 = chatter.createChatSuccess(roomId, new CreateChatRequest("안녕하세요 2", LATITUDE, LONGITUDE));

        List<ChatResponse> response = manager.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getId()).isEqualTo(chat2.getId());
        assertThat(response.get(0).getIsMine()).isFalse();
        assertThat(response.get(1).getId()).isEqualTo(chat1.getId());
        assertThat(response.get(1).getIsMine()).isTrue();
        response = manager.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(0);

        response = chatter.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getId()).isEqualTo(chat2.getId());
        assertThat(response.get(0).getIsMine()).isTrue();
        assertThat(response.get(1).getId()).isEqualTo(chat1.getId());
        assertThat(response.get(1).getIsMine()).isFalse();
        response = chatter.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(0);

        ChatResponse chat3 = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요 3", LATITUDE, LONGITUDE));
        response = manager.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isEqualTo(chat3.getId());
        assertThat(response.get(0).getIsMine()).isTrue();
        response = chatter.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isEqualTo(chat3.getId());
        assertThat(response.get(0).getIsMine()).isFalse();

        TestClient visitor = createTestClient();
        visitor.loginSuccess(KAKAO_AUTH_ID + 2L);
        response = visitor.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(3);
        assertThat(response.get(0).getId()).isEqualTo(chat3.getId());
        assertThat(response.get(0).getIsMine()).isFalse();
        assertThat(response.get(1).getId()).isEqualTo(chat2.getId());
        assertThat(response.get(1).getIsMine()).isFalse();
        assertThat(response.get(2).getId()).isEqualTo(chat1.getId());
        assertThat(response.get(2).getIsMine()).isFalse();
    }

    @DisplayName("유저는 생성된 채팅에 답장을 달 수 있다.")
    @Test
    void parentChat() {
        TestClient manager = createTestClient();
        manager.loginSuccess(KAKAO_AUTH_ID);
        Long roomId = manager.createRoom(new CreateChatRoomRequest(NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));

        TestClient chatter = createTestClient();
        chatter.loginSuccess(KAKAO_AUTH_ID + 1L);
        chatter.createChatterSuccess(roomId, new CreateChatterRequest("검은고양이", LATITUDE, LONGITUDE));

        ChatResponse parentChat = manager.createChatSuccess(roomId, new CreateChatRequest("안녕하세요", LATITUDE, LONGITUDE));
        assertThat(parentChat.getParent()).isNull();
        ChatResponse childChat1 = manager.createChatSuccess(roomId, new CreateChatRequest("본인의 답장", parentChat.getId(), LATITUDE, LONGITUDE));
        assertThat(childChat1.getParent().getId()).isEqualTo(parentChat.getId());
        ChatResponse childChat2 = chatter.createChatSuccess(roomId, new CreateChatRequest("타인의 답장", parentChat.getId(), LATITUDE, LONGITUDE));
        assertThat(childChat2.getParent().getId()).isEqualTo(parentChat.getId());

        List<ChatResponse> response = manager.getChatsSuccess(roomId, LATITUDE, LONGITUDE).getChats();
        assertThat(response).hasSize(3);
        assertThat(response.get(0).getId()).isEqualTo(childChat2.getId());
        assertThat(response.get(0).getContent()).isEqualTo("타인의 답장");
        assertThat(response.get(0).getIsMine()).isFalse();
        assertThat(response.get(0).getParent().getId()).isEqualTo(parentChat.getId());
        assertThat(response.get(0).getParent().getContent()).isEqualTo("안녕하세요");
        assertThat(response.get(0).getParent().getIsMine()).isTrue();

        assertThat(response.get(1).getId()).isEqualTo(childChat1.getId());
        assertThat(response.get(1).getContent()).isEqualTo("본인의 답장");
        assertThat(response.get(1).getIsMine()).isTrue();
        assertThat(response.get(1).getParent().getId()).isEqualTo(parentChat.getId());
        assertThat(response.get(1).getParent().getContent()).isEqualTo("안녕하세요");
        assertThat(response.get(1).getParent().getIsMine()).isTrue();

        assertThat(response.get(2).getId()).isEqualTo(parentChat.getId());
        assertThat(response.get(2).getContent()).isEqualTo("안녕하세요");
        assertThat(response.get(2).getIsMine()).isTrue();
        assertThat(response.get(2).getParent()).isNull();
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
