package com.project.app.post.api.dto;

import com.project.app.post.domain.Timeline;
import com.project.app.post.domain.Post;
import com.project.app.post.domain.TimelinePost;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

// 타임라인 기본화면
public record TimelineListResponse(
        Long timelineId,
        String title,
        Long clubId,                  // 어떤 동아리의 타임라인인지 식별
        String clubName,              // 동아리 이름 (화면 표시용)
        String representativeImageUrl // 타임라인 카드를 채울 대표 이미지 1장
) {
    public static TimelineListResponse from(Timeline timeline) {
        // 날짜(yyyy-MM-dd)와 시간(HH:mm) 포맷 정의
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 💡 1. 내부 피드들을 [날짜가 빠르고 ➡️ 시간이 빠른 순]으로 정렬하여 대표 이미지 추출
        String repUrl = timeline.getTimelinePosts().stream()
                .map(TimelinePost::getPost)
                .filter(post -> post.getImages() != null && !post.getImages().isEmpty())
                // 정렬 조건 추가: LocalDate(날짜) 비교 후 LocalTime(시간) 비교
                .sorted(Comparator.comparing((Post p) -> p.getCreatedAt().toLocalDate())
                        .thenComparing(p -> p.getCreatedAt().toLocalTime()))
                .map(post -> {
                    // 예시 요구사항 반영: 만약 내부 화면단 표시를 위해 나누는 로직이 필요하다면 아래처럼 쪼갤 수 있습니다.
                    String formattedDate = post.getCreatedAt().toLocalDate().format(dateFormatter); // YYYY-MM-DD
                    String formattedTime = post.getCreatedAt().toLocalTime().format(timeFormatter); // HH:mm

                    return post.getImages().get(0);
                })
                .findFirst()
                .orElse("default_timeline_image_url");

        return new TimelineListResponse(
                timeline.getId(),
                timeline.getTitle(),
                timeline.getClub().getId(),
                timeline.getClub().getName(),
                timeline.getRepresentativeImageUrl() // 이미 저장된 값 즉시 매핑!
        );
    }
}