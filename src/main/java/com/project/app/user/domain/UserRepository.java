package com.project.app.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 카카오 고유 식별 번호로 기존 가입 유저가 있는지 찾는 메서드
    Optional<User> findByKakaoId(Long kakaoId);
}