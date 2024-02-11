package com.wafflestudio.babble.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wafflestudio.babble.auth.application.JwtTokenService;
import com.wafflestudio.babble.auth.application.KakaoService;
import com.wafflestudio.babble.auth.presentation.dto.LoginResponse;
import com.wafflestudio.babble.common.utils.TimeUtils;
import com.wafflestudio.babble.member.application.MemberService;
import com.wafflestudio.babble.member.application.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements SwaggerAuthController {

    private final KakaoService kakaoService;
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String code) {
        long kakaoAuthId = kakaoService.getAuthId(code);
        MemberDto member = memberService.getOrCreateMember(kakaoAuthId);
        String accessToken = jwtTokenService.createToken(member.getUserId(), TimeUtils.getCurrentUnixTimestamp());
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }
}
