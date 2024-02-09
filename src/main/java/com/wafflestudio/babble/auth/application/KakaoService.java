package com.wafflestudio.babble.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.wafflestudio.babble.auth.application.dto.KakaoOAuthTokenResponse;
import com.wafflestudio.babble.auth.application.dto.KakaoUserInfoResponse;
import com.wafflestudio.babble.common.exception.BadRequestException;

@Service
public class KakaoService {

    private static final String OAUTH_TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";


    private final WebClient webClient;
    private final String clientId;
    private final String redirectURI;

    public KakaoService(WebClient webClient,
                        @Value("${auth.kakao.clientId}") final String clientId,
                        @Value("${auth.kakao.redirectURI}") final String redirectURI) {
        this.webClient = webClient;
        this.clientId = clientId;
        this.redirectURI = redirectURI;
    }

    public long getAuthId(String code) {
        String token = getToken(code);
        return getUserInfo(token).getId();
    }

    /**
     * <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token">API Reference</a>
     */
    public String getToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", this.clientId);
        formData.add("redirect_uri", this.redirectURI);
        formData.add("code", code);

        KakaoOAuthTokenResponse response = webClient.post()
            .uri(OAUTH_TOKEN_REQUEST_URI)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToFlux(KakaoOAuthTokenResponse.class)
            .blockFirst();
        if (response == null) {
            throw new BadRequestException("Kakao: 인가코드로 토큰을 발급받는 데 실패하였습니다.");
        }
        return response.getAccessToken();
    }

    /**
     * <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info">API Reference</a>
     */
    public KakaoUserInfoResponse getUserInfo(String token) {
        KakaoUserInfoResponse response = webClient.get()
            .uri(USER_INFO_URI)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToFlux(KakaoUserInfoResponse.class)
            .blockFirst();
        if (response == null) {
            throw new BadRequestException("Kakao: 토큰으로 사용자 정보를 조회하는 데 실패하였습니다.");
        }
        return response;
    }
}
