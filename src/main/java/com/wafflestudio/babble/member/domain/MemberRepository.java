package com.wafflestudio.babble.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wafflestudio.babble.common.exception.NotFoundException;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByUserId(String userId) {
        return findByUserId(userId).orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
    }

    boolean existsMemberByUserId(String userId);

    Optional<Member> findByUserId(String userId);
    Optional<Member> findByKakaoAuthId(Long kakaoAuthId);
}
