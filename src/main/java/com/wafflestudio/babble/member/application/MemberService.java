package com.wafflestudio.babble.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wafflestudio.babble.common.exception.InternalServerException;
import com.wafflestudio.babble.common.utils.StringGenerator;
import com.wafflestudio.babble.common.utils.RandomUtils;
import com.wafflestudio.babble.member.application.dto.MemberDto;
import com.wafflestudio.babble.member.domain.Member;
import com.wafflestudio.babble.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDto getOrCreateMember(Long kakaoAuthId) {
        return memberRepository.findByKakaoAuthId(kakaoAuthId)
            .map(MemberDto::of)
            .orElseGet(() -> createMember(kakaoAuthId, RandomUtils::generateRandomAlphanumericString));
    }

    public MemberDto createMember(Long kakaoAuthId, StringGenerator generator) {
        for (int i = 0; i < 999; i++) {
            String userId = generator.generateRandomAlphanumericString(10);
            if (memberRepository.existsMemberByUserId(userId)) {
                continue;
            }
            Member newMember = Member.create(userId, kakaoAuthId);
            return MemberDto.of(memberRepository.save(newMember));
        }
        throw new InternalServerException("Max retry count reached");
    }
}
