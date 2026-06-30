package com.project.app.kakao.api;

import com.project.app.auth.api.dto.TokenResponse;
import com.project.app.auth.application.AuthService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.kakao.api.dto.KakaoUserInfoResponse;
import com.project.app.kakao.application.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    @GetMapping("/kakao")
    public ResponseEntity<ApiResTemplate<TokenResponse>> kakaoLogin(@RequestParam("code") String code) {
        // 1. 카카오 서버와 통신하여 유저 정보(ID, 이미지 URL 등)를 통째로 가져오기
        KakaoUserInfoResponse kakaoUserInfo = kakaoOAuthService.getKakaoUserProfile(code);

        // 2. DTO에서 kakaoId와 profileImageUrl을 각각 꺼내어 로그인/회원가입 진행
        TokenResponse tokenResponse = authService.loginOrSignUp(
                kakaoUserInfo.getId(),
                kakaoUserInfo.getProfileImageUrl()
        );

        // 3. 공통 응답 바구니(ApiResTemplate) 및 성공 코드(SuccessCode) 양식에 맞춰 프론트엔드로 반환
        return ResponseEntity
                .status(SuccessCode.LOGIN_SUCCESS.getHttpStatus())
                .body(ApiResTemplate.success(
                        SuccessCode.LOGIN_SUCCESS.getHttpStatusCode(),
                        SuccessCode.LOGIN_SUCCESS.getMessage(),
                        tokenResponse
                ));
    }
}