package com.project.app.auth.application;

import com.project.app.auth.api.dto.TokenResponse;
import com.project.app.auth.domain.JwtUtil;
import com.project.app.user.domain.User;
import com.project.app.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 카카오 ID를 검사하여 가입 혹은 로그인을 처리하고 토큰을 발급합니다.
     */
    @Transactional
    public TokenResponse loginOrSignUp(Long kakaoId) {
        String stringKakaoId = String.valueOf(kakaoId);

        // 1. DB에서 해당 카카오 ID 유저가 있는지 검색
        User user = userRepository.findByKakaoId(stringKakaoId)
                .orElseGet(() -> {
                    // 2. 없다면 신규 유저로 등록 (자동 회원가입)
                    User newUser = new User();
                    newUser.setKakaoId(stringKakaoId);
                    return userRepository.save(newUser); // DB에 영구 저장
                });

        // 3. 저장되거나 조회된 유저의 서비스 고유 고유번호(PK)로 JWT 토큰 생성
        String accessToken = jwtUtil.createAccessToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        // 4. 생성된 토큰 바구니 반환
        return new TokenResponse(accessToken, refreshToken);
    }
}