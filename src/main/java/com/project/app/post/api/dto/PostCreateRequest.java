package com.project.app.post.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// 생성, 수정
public record PostCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        String content,

        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl,

        @NotNull(message = "활동 날짜는 필수입니다.")
        LocalDate activityDate
) {}