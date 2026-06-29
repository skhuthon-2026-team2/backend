package com.project.app.user.domain;

import jakarta.persistence.*;
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
    private String kakaoId;

    @Column(nullable = true) // 후순위 (Null 허용)
    private String email;

    @Column(nullable = true) // 후순위 (Null 허용)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 가입 일시 자동 저장
}