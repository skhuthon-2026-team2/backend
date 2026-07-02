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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF 보안 방어 기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 3. 세션 정책을 STATELESS(무상태)로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. API 요청 주소별 접근 허용/차단 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login/kakao",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 5. 시큐리티 기본 인증 필터 앞에 커스텀 JwtFilter 배치
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS 설정을 정의하는 메서드입니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 요청을 허용할 프론트엔드 도메인 주소들을 등록합니다. (끝에 주소 슬래시(/)나 상세 경로는 제외해야 합니다)
        configuration.setAllowedOrigins(Arrays.asList(
                "https://front-gn7x.vercel.app",
                "http://localhost:5173"
        ));

        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 허용할 헤더 설정 (클라이언트가 보낼 수 있는 헤더 정보)
        configuration.setAllowedHeaders(List.of("*"));

        // 자격 증명(쿠키, Authorization 토큰 등)을 포함한 요청을 허용할지 여부
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 백엔드의 모든 API 경로(/**)에 대해 위 설정을 적용합니다.
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }
}