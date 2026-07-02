package com.project.app.user.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "내가 작성한 피드 목록 응답 DTO")
public record MyFeedListResponse(

        @Schema(description = "조회한 유저의 ID", example = "1")
        Long userId,

        @Schema(description = "작성한 피드 목록")
        List<FeedDto> feeds
) {
    @Schema(description = "개별 피드 정보")
    public record FeedDto(

            @Schema(description = "피드 고유 ID", example = "100")
            Long feedId,

            @Schema(description = "피드 제목", example = "첫 동아리 모임 성공적!")
            String title,

            @Schema(description = "피드 내용", example = "오늘 다같이 피자 먹고 즐거웠습니다. 다음 모임이 기대되네요.")
            String content,

            @Schema(description = "작성 일시", example = "2023-11-20T14:30:00")
            String createdAt
    ) {}
}