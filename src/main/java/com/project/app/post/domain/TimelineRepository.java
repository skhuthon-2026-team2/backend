package com.project.app.post.domain; // 프로젝트 구조에 맞게 패키지명을 맞춰주세요.

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    // 💡 특정 동아리에 생성된 타임라인 목록을 페이징하여 조회합니다. (첫 번째 사진 목록용)
    Page<Timeline> findAllByClubId(Long clubId, Pageable pageable);


}