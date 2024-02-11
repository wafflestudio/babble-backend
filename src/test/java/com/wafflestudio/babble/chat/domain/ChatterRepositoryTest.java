package com.wafflestudio.babble.chat.domain;

import static com.wafflestudio.babble.chat.domain.ChatRoomHashTag.LECTURE_ROOM;
import static com.wafflestudio.babble.testutil.TestFixtures.LOCATION;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;
import com.wafflestudio.babble.testutil.RepositoryTest;

@RepositoryTest
public class ChatterRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatterRepository chatterRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("채팅방에 특정 유저가")
    class GenerateRandomAlphanumericStringTest {

        private Member member;
        private ChatRoom chatRoom;

        @BeforeEach
        public void setUp() {
            member = memberRepository.save(Member.create("abc123", null));
            chatRoom = chatRoomRepository.save(ChatRoom.create(ROOM_NAME, member, LECTURE_ROOM, LOCATION));
        }

        @Test
        @DisplayName("참가자인 경우 참을 반환한다.")
        void existsByRoomIdAndMemberId() {
            chatterRepository.save(Chatter.create(chatRoom, member, NICKNAME));
            boolean actual = chatterRepository.existsByRoomIdAndMemberId(chatRoom.getId(), member.getId());
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("참가하지 않은 경우 거짓을 반환한다.")
        void notExistsByRoomIdAndMemberId() {
            boolean actual = chatterRepository.existsByRoomIdAndMemberId(chatRoom.getId(), member.getId());
            assertThat(actual).isFalse();
        }
    }
}
