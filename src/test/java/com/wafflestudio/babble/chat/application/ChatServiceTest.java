package com.wafflestudio.babble.chat.application;

import static com.wafflestudio.babble.testutil.TestFixtures.CHAT_CONTENT;
import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG;
import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static com.wafflestudio.babble.testutil.TestFixtures.LATITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.LONGITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static com.wafflestudio.babble.testutil.TestFixtures.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.chat.application.dto.ChatDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomDetailDto;
import com.wafflestudio.babble.chat.application.dto.ChatRoomResponseDto;
import com.wafflestudio.babble.chat.application.dto.ChatterDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;
import com.wafflestudio.babble.chat.application.dto.CreateChatterDto;
import com.wafflestudio.babble.chat.application.dto.GetChatRoomDto;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoom;
import com.wafflestudio.babble.chat.domain.chatroom.ChatRoomRepository;
import com.wafflestudio.babble.chat.domain.chatter.Chatter;
import com.wafflestudio.babble.chat.domain.chatter.ChatterRepository;
import com.wafflestudio.babble.common.exception.BadRequestException;
import com.wafflestudio.babble.common.exception.ForbiddenException;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;
import com.wafflestudio.babble.testutil.ServiceTest;

public class ChatServiceTest extends ServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatterRepository chatterRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("새로운 채팅방을 생성하면서 방장 역할의 참여자를 생성한다")
    void createChatRoomTest() {
        Member newMember = memberRepository.save(Member.create(USER_ID, KAKAO_AUTH_ID));

        Long roomId = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        entityManager.clear();
        ChatRoom room = getChatRoom();
        Chatter chatter = getChatter();

        assertThat(room.getId()).isEqualTo(roomId);
        assertThat(room.getManagerId()).isEqualTo(newMember.getId());
        assertThat(chatter.getRoomId()).isEqualTo(roomId);
        assertThat(chatter.getMemberId()).isEqualTo(newMember.getId());
    }

    @Nested
    @DisplayName("채팅방을 조회하면")
    class GetChatRoomTest {

        private Member manager;
        private Long roomId;

        @BeforeEach
        public void setUp() {
            manager = memberRepository.save(Member.create(USER_ID, KAKAO_AUTH_ID));
            roomId = chatService.createChatRoom(CreateChatRoomDto.of(manager.getUserId(), NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        }

        @Test
        @DisplayName("채팅방 참여 여부와 최근 채팅들의 내용 및 작성 시점을 조회할 수 있다")
        void checkIsChatterAndChats() {
            ChatDto chat1 = chatService.createChat(newCreateChatDto(manager, roomId, "안녕하세요"));
            ChatDto chat2 = chatService.createChat(newCreateChatDto(manager, roomId, "달이 아름답군요"));
            ChatDto chat3 = chatService.createChat(newCreateChatDto(manager, roomId, "그럼 이만 나갑니다"));

            ChatRoomDetailDto dto = chatService.getChatRoom(GetChatRoomDto.of(manager.getUserId(), roomId, LATITUDE, LONGITUDE));

            assertThat(dto.getRoom().getId()).isEqualTo(roomId);
            assertThat(dto.getIsChatter()).isTrue();
            assertThat(dto.getChats()).hasSize(3);
            assertThat(dto.getChats().get(0)).isEqualTo(chat3);
            assertThat(dto.getChats().get(1)).isEqualTo(chat2);
            assertThat(dto.getChats().get(2)).isEqualTo(chat1);
        }

        @Test
        @DisplayName("채팅방에 참여 중이지 않아도 동일한 내용을 조회할 수는 있다")
        void outsiderTest() {
            Member member = memberRepository.save(Member.create(USER_ID + "!", null));
            ChatDto chat1 = chatService.createChat(newCreateChatDto(manager, roomId, "안녕하세요"));
            ChatDto chat2 = chatService.createChat(newCreateChatDto(manager, roomId, "달이 아름답군요"));
            ChatDto chat3 = chatService.createChat(newCreateChatDto(manager, roomId, "그럼 이만 나갑니다"));

            ChatRoomDetailDto dto = chatService.getChatRoom(GetChatRoomDto.of(member.getUserId(), roomId, LATITUDE, LONGITUDE));

            assertThat(dto.getRoom().getId()).isEqualTo(roomId);
            assertThat(dto.getIsChatter()).isFalse();
            assertThat(dto.getChats()).hasSize(3);
            assertThat(dto.getChats().get(0)).isEqualTo(chat3);
            assertThat(dto.getChats().get(1)).isEqualTo(chat2);
            assertThat(dto.getChats().get(2)).isEqualTo(chat1);
        }
    }

    @Test
    @DisplayName("현재 위치를 기준으로 채팅방 목록을 조회한다")
    void getNearbyRoomsTest() {
        memberRepository.save(Member.create(USER_ID, KAKAO_AUTH_ID));
        Long room1 = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, NICKNAME, ROOM_NAME, HASHTAG, 10.0d, 10.0d));
        Long room2 = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, NICKNAME, ROOM_NAME, HASHTAG, 10.1d, 10.0d));
        Long room3 = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, NICKNAME, ROOM_NAME, HASHTAG, 10.0d, 10.1d));

        // TODO: 위치 기반 필터링 구현 후 테스트도 수정하기
        List<ChatRoomResponseDto> rooms = chatService.getNearbyRooms(10.0d, 10.0d);

        assertThat(rooms.size()).isEqualTo(3);
        assertThat(rooms.get(0).getId()).isEqualTo(room1);
        assertThat(rooms.get(1).getId()).isEqualTo(room2);
        assertThat(rooms.get(2).getId()).isEqualTo(room3);
    }

    @Nested
    @DisplayName("채팅방에")
    class CreateChatterTest {

        private static final String MANAGER_NICKNAME = NICKNAME;

        private Long roomId;
        private String memberUserId;

        @BeforeEach
        public void setUp() {
            memberRepository.save(Member.create(USER_ID, KAKAO_AUTH_ID));
            roomId = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, MANAGER_NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
            Member member = memberRepository.save(Member.create(USER_ID + "!", null));
            memberUserId = member.getUserId();
        }

        @Test
        @DisplayName("참여자가 될 수 있다")
        void success() {
            ChatterDto dto = chatService.createChatter(CreateChatterDto.of(memberUserId, roomId, "토끼", LATITUDE, LONGITUDE));
            assertThat(dto).isNotNull();
        }

        @Test
        @DisplayName("이미 참여 중인 경우 참여자가 될 수 없다")
        void alreadyCreated() {
            chatService.createChatter(CreateChatterDto.of(memberUserId, roomId, "토끼", LATITUDE, LONGITUDE));

            assertThatThrownBy(() -> chatService.createChatter(CreateChatterDto.of(memberUserId, roomId, "토끼!", LATITUDE, LONGITUDE)))
                .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("다른 사람이 사용 중인 닉네임은 사용할 수 없다")
        void duplicateNickname() {
            assertThatThrownBy(() -> chatService.createChatter(CreateChatterDto.of(memberUserId, roomId, MANAGER_NICKNAME, LATITUDE, LONGITUDE)))
                .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("채팅방에")
    class CreateChatTest {

        private Member manager;
        private Long roomId;

        @BeforeEach
        public void setUp() {
            manager = memberRepository.save(Member.create(USER_ID, KAKAO_AUTH_ID));
            roomId = chatService.createChatRoom(CreateChatRoomDto.of(USER_ID, NICKNAME, ROOM_NAME, HASHTAG, LATITUDE, LONGITUDE));
        }

        @Test
        @DisplayName("참여 중인 경우 채팅을 생성할 수 있다")
        void isChatterSuccess() {
            ChatDto dto = chatService.createChat(newCreateChatDto(manager, roomId, CHAT_CONTENT));
            assertThat(dto).isNotNull();
        }

        @Test
        @DisplayName("아직 참여 중이지 않은 경우 채팅을 생성할 수 없다")
        void notChatterForbidden() {
            Member anotherMember = memberRepository.save(Member.create(USER_ID + "!", null));

            assertThatThrownBy(() -> chatService.createChat(newCreateChatDto(anotherMember, roomId, CHAT_CONTENT)))
                .isInstanceOf(ForbiddenException.class);
        }
    }

    private ChatRoom getChatRoom() {
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        assertThat(rooms).hasSize(1);
        return rooms.get(0);
    }

    private Chatter getChatter() {
        List<Chatter> chatters = chatterRepository.findAll();
        assertThat(chatters).hasSize(1);
        return chatters.get(0);
    }

    private CreateChatDto newCreateChatDto(Member member, Long roomId, String content) {
        return CreateChatDto.of(member.getUserId(), roomId, content, LATITUDE, LONGITUDE);
    }
}
