package com.project.app.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 동아리의 전체 피드 목록 조회 (최신순 페이징)
    Page<Post> findAllByClubId(Long clubId, Pageable pageable);

    // 특정 유저(로그인ID 기준)가 작성한 피드 목록 조회 (최신순 페이징)
    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}