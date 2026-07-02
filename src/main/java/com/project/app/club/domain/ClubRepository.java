package com.project.app.club.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // 초대 코드로 동아리를 찾는 기능 (초대 수락 기능 구현 시 필수)
    Optional<Club> findByInviteCode(String inviteCode);

    // 해당 초대 코드가 이미 존재하는지 확인 (중복 생성 방지용)
    boolean existsByInviteCode(String inviteCode);
}
