package com.project.app.kakao.api;

import com.project.app.auth.api.dto.TokenResponse;
import com.project.app.auth.application.AuthService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.kakao.application.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService; // 서비스 연결

    @GetMapping("/kakao")
    public ResponseEntity<ApiResTemplate<TokenResponse>> kakaoLogin(@RequestParam("code") String code) {
        // 1. 카카오 서버와 통신하여 고유 식별 번호 가져오기
        Long kakaoId = kakaoOAuthService.getKakaoUserId(code);

        // 2. 해당 고유 번호로 회원 검증 및 우리 서비스 전용 JWT 토큰 생성
        TokenResponse tokenResponse = authService.loginOrSignUp(kakaoId);

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