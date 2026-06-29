package com.project.app.auth.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보안 방어 기능 비활성화
                // 세션을 쓰지 않고 웹브라우저 쿠키를 통한 자동 로그인 기능도 안 쓰므로 REST API 환경에서는 필요 없습니다.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 세션 정책을 STATELESS(무상태)로 설정
                // 서버가 로그인 상태를 기억하는 '세션'을 유지하지 않고, 매번 들어오는 'JWT 토큰'만 검사합니다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. API 요청 주소별 접근 허용/차단 설정
                .authorizeHttpRequests(auth -> auth
                        // 카카오 로그인 인증창 주소 및 스웨거 명세서 전용 주소들은 로그인 없이 전면 허용(permitAll)합니다.
                        .requestMatchers(
                                "/api/auth/login/kakao",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 그 외 동아리 생성, 가입, 피드 작성 등 나머지 모든 API 주소는 토큰 인증을 요구합니다.
                        .anyRequest().authenticated()
                )

                // 4. 시큐리티 기본 인증 필터 앞에 우리가 커스텀하게 만든 JwtFilter를 문지기로 배치
                // 스프링 시큐리티가 원래 작동하던 아이디/비밀번호 확인 절차를 거치기 전에 토큰 검사를 선수 쳐서 진행합니다.
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}