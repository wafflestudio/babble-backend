package com.wafflestudio.babble.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.testutil.RepositoryTest;

@RepositoryTest
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("개별 회원은 고유한 ID로 식별되며, 소셜 로그인 계정 ID는 선택사항이다.")
    void userIdAndAuthId() {
        memberRepository.save(new Member(null, "abc123", null));
        memberRepository.save(new Member(null, "abc124", 12345678L));
        memberRepository.save(new Member(null, "abc125", 12345679L));
        entityManager.clear();

        List<Member> members = memberRepository.findAll();

        assertThat(members).hasSize(3);
    }
}
