package com.project.app.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId; // 카카오 고유 식별 번호

    @Column(nullable = false)
    private String nickname; // 카카오 사용자 이름 추가

    @Column(nullable = false)
    private String imageUrl; // 프로필 이미지 URL (카카오 기본값 혹은 수정된 값)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 최초 가입 날짜 추가

    // 🔥 추가: 영속화 직전 자동 실행 규칙 심기
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public User(Long kakaoId, String nickname, String imageUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    /**
     * 프로필 이미지 수정 기능 (요구사항: 이미지만 수정 가능)
     */
    public void updateProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrl = imageUrl;
        }
    }

    public void updateProfile(String nickname, String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            this.imageUrl = imageUrl;
        }
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
    }
}