package com.project.app.post.api.dto;

import com.project.app.post.domain.Timeline;
import com.project.app.post.domain.Post;
import java.time.LocalDateTime;
import java.util.List;

//타임라인 크게 보기
public record TimelineDetailResponse(
        Long timelineId,
        String title,
        List<SimplePostDto> posts // 타임라인에 포함된 게시글 상세 리스트
) {
    public static TimelineDetailResponse from(Timeline timeline) {
        List<SimplePostDto> postDtos = timeline.getPosts().stream()
                .map(SimplePostDto::from)
                .toList();

        return new TimelineDetailResponse(
                timeline.getId(),
                timeline.getTitle(),
                postDtos
        );
    }

    // 상세 화면 내부에 나열될 게시글 전용 경량 DTO
    public record SimplePostDto(
            Long postId,
            String title,
            String content,
            LocalDateTime createdAt,
            List<String> imageUrls
    ) {
        public static SimplePostDto from(Post post) {
            return new SimplePostDto(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getImages()
            );
        }
    }
}