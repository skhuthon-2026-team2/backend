package com.project.app.user.api.dto;

import java.util.List;

// 💡 나중에 Feed 엔티티가 구현되면 데이터 매핑 로직을 채워넣으시면 됩니다.
public record MyFeedListResponse(
        Long userId,
        List<FeedDto> feeds
) {
    public record FeedDto(
            Long feedId,
            String title,
            String content,
            String createdAt
    ) {}
}