package com.project.app.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 동아리의 전체 피드 목록 조회 (ClubMember를 거쳐 clubId 확인)
    Page<Post> findAllByClubMemberClubId(Long clubId, Pageable pageable);

    // 특정 유저가 작성한 피드 목록 조회 (ClubMember를 거쳐 userId 확인)
    Page<Post> findAllByClubMemberUserId(Long userId, Pageable pageable);

    // 멤버 삭제 시 외래키(club_member_id)를 null로 밀어버리는 쿼리
    @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트를 자동으로 비워줍니다.
    @Query("UPDATE Post p SET p.clubMember = null WHERE p.clubMember.id = :clubMemberId")
    void updateClubMemberToNull(@Param("clubMemberId") Long clubMemberId);

    // 💡 특정 동아리 안에서, 지정된 시간(하루의 시작과 끝) 사이에 생성된 게시글 목록을 조회합니다. (두 번째 사진 날짜 필터링용)
    // 💡 JPQL을 사용해 clubMember 내부의 club.id를 명확히 짚어줍니다.
    @Query("SELECT p FROM Post p WHERE p.clubMember.club.id = :clubId AND p.createdAt BETWEEN :start AND :end")
    List<Post> findAllByClubIdAndCreatedAtBetween(
            @Param("clubId") Long clubId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}