package com.project.app.post.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TimelineCreateRequest(
        @NotBlank(message = "타임라인 제목은 필수입니다.")
        String title,                  // 타임라인 제목

        String representativeImageUrl, // 사용자가 선택한 대표 이미지 URL

        @NotEmpty(message = "최소 하나 이상의 게시글을 선택해야 합니다.")
        List<PostOrderRequest> posts   // 순서 정보가 포함된 게시글 리스트
) {
    public record PostOrderRequest(
            Long postId,            // 게시글(피드) ID
            int sequence            // 슬라이드 순서 (1, 2, 3...)
    ) {}
}