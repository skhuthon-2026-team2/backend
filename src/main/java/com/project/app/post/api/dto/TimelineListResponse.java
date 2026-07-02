package com.project.app.post.api.dto;

import com.project.app.post.domain.Timeline;
import com.project.app.post.domain.Post;

// 타임라인 기본화면
public record TimelineListResponse(
        Long timelineId,
        String title,
        Long clubId,                  // 🔥 추가: 어떤 동아리의 타임라인인지 식별
        String clubName,              // 🔥 추가: 동아리 이름 (화면 표시용)
        String representativeImageUrl // 타임라인 카드를 채울 대표 이미지 1장
) {
    public static TimelineListResponse from(Timeline timeline) {
        // 타임라인에 묶인 게시글이 있고, 그 게시글에 이미지가 있다면 가장 첫 번째 이미지를 대표 이미지로 설정
        String repUrl = timeline.getPosts().stream()
                .filter(post -> post.getImages() != null && !post.getImages().isEmpty())
                .map(post -> post.getImages().get(0)) // 첫 번째 게시글의 첫 사진
                .findFirst()
                .orElse("default_timeline_image_url"); // 사진이 없는 경우 기본 이미지

        return new TimelineListResponse(
                timeline.getId(),
                timeline.getTitle(),
                timeline.getClub().getId(),
                timeline.getClub().getName(), // Club 엔티티의 이름 필드 매핑
                repUrl
        );
    }
}