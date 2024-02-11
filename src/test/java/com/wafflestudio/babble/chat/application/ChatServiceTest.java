package com.wafflestudio.babble.chat.application;

import static com.wafflestudio.babble.testutil.TestFixtures.HASHTAG;
import static com.wafflestudio.babble.testutil.TestFixtures.KAKAO_AUTH_ID;
import static com.wafflestudio.babble.testutil.TestFixtures.LATITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.LONGITUDE;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static com.wafflestudio.babble.testutil.TestFixtures.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.chat.application.dto.CreateChatRoomDto;
import com.wafflestudio.babble.chat.domain.ChatRoom;
import com.wafflestudio.babble.chat.domain.ChatRoomRepository;
import com.wafflestudio.babble.chat.domain.Chatter;
import com.wafflestudio.babble.chat.domain.ChatterRepository;
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
}
