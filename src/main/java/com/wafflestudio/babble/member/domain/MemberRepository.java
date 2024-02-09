package com.wafflestudio.babble.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByUserId(String userId);

    Optional<Member> findByKakaoAuthId(Long kakaoAuthId);
}
