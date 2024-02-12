package com.wafflestudio.babble.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wafflestudio.babble.auth.application.dto.KakaoUserInfoResponse;
import com.wafflestudio.babble.common.exception.BadRequestException;

@Service
public class KakaoService {

    /**
     * <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info">API Reference</a>
     */
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final WebClient webClient;

    public KakaoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Long getUserId(String token) {
        KakaoUserInfoResponse response = webClient.get()
            .uri(USER_INFO_URI)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(KakaoUserInfoResponse.class)
            .blockFirst();
        if (response == null) {
            throw new BadRequestException("Kakao: 토큰으로 사용자 정보를 조회하는 데 실패하였습니다.");
        }
        return response.getId();
    }
}
