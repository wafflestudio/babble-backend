package com.wafflestudio.babble.chat.domain;

import static com.wafflestudio.babble.chat.domain.ChatRoomHashTag.LECTURE_ROOM;
import static com.wafflestudio.babble.testutil.TestFixtures.FUTURE_UNIX_TIME_MS;
import static com.wafflestudio.babble.testutil.TestFixtures.LOCATION;
import static com.wafflestudio.babble.testutil.TestFixtures.NICKNAME;
import static com.wafflestudio.babble.testutil.TestFixtures.PAST_UNIX_TIME_MS;
import static com.wafflestudio.babble.testutil.TestFixtures.ROOM_NAME;
import static com.wafflestudio.babble.testutil.TestFixtures.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatterRepository chatterRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("채팅방에서 특정 유저가")
    class ExistsByRoomAndMemberTest {

        private Member member;
        private ChatRoom chatRoom;

        @BeforeEach
        public void setUp() {
            member = memberRepository.save(Member.create(USER_ID, null));
            chatRoom = chatRoomRepository.save(ChatRoom.create(ROOM_NAME, member, LECTURE_ROOM, LOCATION));
        }

        @Test
        @DisplayName("참가자인 경우 참을 반환한다.")
        void exists() {
            chatterRepository.save(Chatter.create(chatRoom, member, NICKNAME));
            boolean actual = chatterRepository.existsByRoomAndMember(chatRoom, member);
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("참가하지 않은 경우 거짓을 반환한다.")
        void notExists() {
            boolean actual = chatterRepository.existsByRoomAndMember(chatRoom, member);
            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("채팅방에서 채팅이 생성되면")
    class TimeTest {

        private Member member;
        private ChatRoom chatRoom;

        @BeforeEach
        public void setUp() {
            member = memberRepository.save(Member.create(USER_ID, null));
            chatRoom = chatRoomRepository.save(ChatRoom.create(ROOM_NAME, member, LECTURE_ROOM, LOCATION));
        }

        @Test
        @DisplayName("생성 시점 정보가 유닉스 시간으로 자동 저장된다.")
        void createdAtAutoSaved() {
            Chatter chatter = chatterRepository.save(Chatter.create(chatRoom, member, NICKNAME));

            assertThat(chatter.getCreatedAt()).isGreaterThan(PAST_UNIX_TIME_MS);
            assertThat(chatter.getCreatedAt()).isLessThan(FUTURE_UNIX_TIME_MS);
            assertThat(chatter.getCreatedAt()).isEqualTo(chatter.getUpdatedAt());
        }

        @Test
        @DisplayName("수정 시점 정보가 유닉스 시간으로 자동 업데이트된다.")
        void createdAtAutoUpdated() {
            Chatter chatter = chatterRepository.save(Chatter.create(chatRoom, member, NICKNAME));
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
            chatter.updateNickname("작은하마");
            chatter = chatterRepository.save(chatter);
            entityManager.flush();

            chatter = chatterRepository.findById(chatter.getId()).orElseThrow();
            assertThat(chatter.getUpdatedAt()).isGreaterThan(PAST_UNIX_TIME_MS);
            assertThat(chatter.getUpdatedAt()).isLessThan(FUTURE_UNIX_TIME_MS);
            assertThat(chatter.getCreatedAt()).isLessThan(chatter.getUpdatedAt());
        }
    }
}
