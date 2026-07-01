package com.project.app.post.api.dto;

import com.project.app.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

// 날짜별 피드 조회 응답 DTO
public record TimelineFeedResponse(
        Long postId,
        String title,
        LocalDateTime createdAt,
        List<String> imageUrls         // 한 게시글에 사진 여러 장 처리 가능하도록 리스트 구조화
) {
    public static TimelineFeedResponse from(Post post) {
        return new TimelineFeedResponse(
                post.getId(),
                post.getTitle(),
                post.getCreatedAt(),
                post.getImages() // Post 엔티티 내부의 이미지 URL 리스트 가져오기
        );
    }
}