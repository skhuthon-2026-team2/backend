package com.project.app.post.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "피드(게시글) 생성 및 수정 요청 DTO")
public record PostCreateRequest(

        @Schema(description = "피드 제목", example = "즐거운 동아리 첫 모임!")
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @Schema(description = "피드 내용", example = "다들 반가웠어요! 다음에도 또 봐요~")
        String content,

        @Schema(description = "업로드할 활동 이미지 URL", example = "https://dummyimage.com/post.jpg")
        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl,

        @Schema(description = "실제 활동 날짜", example = "2023-11-20")
        @NotNull(message = "활동 날짜는 필수입니다.")
        LocalDate activityDate
) {}