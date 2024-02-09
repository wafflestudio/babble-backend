package com.wafflestudio.babble.member.application.dto;

import com.wafflestudio.babble.member.domain.Member;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MemberDto {

    private final Long id;
    private final String userId;
    private final Long kakaoAuthId;

    private MemberDto(Long id, String userId, Long kakaoAuthId) {
        this.id = id;
        this.userId = userId;
        this.kakaoAuthId = kakaoAuthId;
    }

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getUserId(), member.getKakaoAuthId());
    }
}
