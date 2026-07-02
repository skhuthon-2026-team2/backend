package com.project.app.post.api.dto;

import com.project.app.post.domain.Timeline;
import com.project.app.post.domain.Post;
import com.project.app.post.domain.TimelinePost;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

//타임라인 상세 보기
public record TimelineDetailResponse(
        Long timelineId,
        String title,
        int duration, // 재생 시간 필드 추가
        List<SimplePostDto> posts // 타임라인에 포함된 게시글 상세 리스트
) {
    public static TimelineDetailResponse from(Timeline timeline) {
        // 기존 timeline.getPosts() 대신 timeline.getTimelinePosts()를 가져와 sequence 순으로 정렬합니다.
        List<SimplePostDto> postDtos = timeline.getTimelinePosts().stream()
                .sorted(Comparator.comparingInt(TimelinePost::getSequence)) // 순서 오름차순 정렬 (1, 2, 3...)
                .map(timelinePost -> SimplePostDto.from(timelinePost.getPost())) // 중간 엔티티에서 진짜 Post를 꺼내 변환
                .toList();

        return new TimelineDetailResponse(
                timeline.getId(),
                timeline.getTitle(),
                timeline.getDuration(), // 저장된 재생 시간 바구니에 매핑
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