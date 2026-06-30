package com.project.app.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 동아리의 전체 피드 목록 조회 (ClubMember를 거쳐 clubId 확인)
    Page<Post> findAllByClubMemberClubId(Long clubId, Pageable pageable);

    // 특정 유저가 작성한 피드 목록 조회 (ClubMember를 거쳐 userId 확인)
    Page<Post> findAllByClubMemberUserId(Long userId, Pageable pageable);
}