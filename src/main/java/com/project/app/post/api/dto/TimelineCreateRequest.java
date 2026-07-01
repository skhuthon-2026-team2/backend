package com.project.app.post.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

// 타임라인 생성 요청 DTO
public record TimelineCreateRequest(
        @NotBlank String title,       // 타임라인 제목
        List<Long> postIds            // 체크박스에서 선택된 게시글 ID 리스트
) {}

