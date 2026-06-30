package com.project.app.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter // 프로필 수정을 위해 Setter를 열어둡니다.
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId; // 카카오 고유 식별 번호

    @Column(nullable = false)
    private String imageUrl; // 프로필 이미지 URL (카카오 기본값 혹은 수정된 값)

    @Builder
    public User(Long kakaoId, String imageUrl) {
        this.kakaoId = kakaoId;
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
}