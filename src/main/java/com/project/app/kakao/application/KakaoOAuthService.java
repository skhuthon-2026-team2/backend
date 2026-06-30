package com.project.app.kakao.application;

import com.project.app.kakao.api.dto.KakaoUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class KakaoOAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 카카오 로그인 전체 흐름 제어 및 카카오 고유 ID 반환
     */
    public Long getKakaoUserId(String code) {
        // 1. 인가 코드로 카카오 Access Token 받아오기
        String kakaoAccessToken = getKakaoAccessToken(code);

        // 2. Access Token으로 카카오 유저 정보 가져오기
        return getKakaoUserInfo(kakaoAccessToken);
    }

    /**
     * [Step 1] 인가 코드로 카카오 토큰 발급 요청
     */
    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            log.error("카카오 토큰 발급 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 로그인 중 오류가 발생했습니다.");
        }
    }

    /**
     * [Step 2] 카카오 토큰으로 유저 정보(ID) 조회
     */
    private Long getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // 헤더에 Bearer 토큰 주입
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                    userInfoUri, HttpMethod.POST, request, KakaoUserInfoResponse.class
            );
            return response.getBody().getId();
        } catch (Exception e) {
            log.error("카카오 유저 정보 조회 실패: {}", e.getMessage());
            throw new RuntimeException("카카오 유저 정보를 가져오는데 실패했습니다.");
        }
    }
}