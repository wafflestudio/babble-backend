package com.wafflestudio.babble.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wafflestudio.babble.common.exception.InternalServerException;
import com.wafflestudio.babble.common.utils.StringGenerator;
import com.wafflestudio.babble.member.application.dto.MemberDto;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;
import com.wafflestudio.babble.testutil.ServiceTest;

@DisplayName("AuthService 클래스의")
public class MemberServiceTest extends ServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("소셜 로그인 계정 ID에 대응되는 회원이 없으면 생성하고, 존재하면 그대로 조회한다.")
    void getOrCreateMemberTest() {
        long kakaoAuthId = 1L;

        MemberDto memberDto1 = memberService.getOrCreateMember(kakaoAuthId);
        entityManager.clear();
        MemberDto memberDto2 = memberService.getOrCreateMember(kakaoAuthId);
        entityManager.clear();
        MemberDto memberDto3 = memberService.getOrCreateMember(kakaoAuthId);
        entityManager.clear();
        List<Member> members = memberRepository.findAll();

        assertThat(memberDto1.getKakaoAuthId()).isEqualTo(kakaoAuthId);
        assertThat(memberDto1).isEqualTo(memberDto2);
        assertThat(memberDto1).isEqualTo(memberDto3);
        assertThat(members).hasSize(1);
    }

    @Test
    @DisplayName("이미 존재하는 ID로 회원을 생성하려는 경우 일정 횟수만큼만 재시도하고 에러를 반환한다.")
    void createMemberRetryTest() {
        StringGenerator sameStringGenerator = (int length) -> "sameUserId";
        memberService.createMember(1L, sameStringGenerator);

        assertThatThrownBy(() -> memberService.createMember(2L, sameStringGenerator))
            .isInstanceOf(InternalServerException.class);
    }
}
