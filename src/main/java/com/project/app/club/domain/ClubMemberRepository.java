package com.project.app.club.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    // 특정 유저가 가입한 동아리 목록 조회 (페이징 적용)
    Page<ClubMember> findAllByUserId(Long userId, Pageable pageable);

    // 특정 동아리의 회원 목록 조회 (페이징 적용)
    Page<ClubMember> findAllByClubId(Long clubId, Pageable pageable);

    // 이미 가입된 회원인지 확인 (중복 가입 방지용)
    boolean existsByClubIdAndUserId(Long clubId, Long userId);

    // 특정 동아리에서 특정 유저 한 명의 정보를 찾기 (추방이나 권한 변경 시 필요)
    Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId);

    // 특정 동아리에 가입한 멤버 총 수를 구하는 쿼리 메서드
    long countByClubId(Long clubId);
}
