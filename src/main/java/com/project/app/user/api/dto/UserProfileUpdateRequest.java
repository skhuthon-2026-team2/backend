package com.project.app.user.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

// 유저 프로필 이미지(기본 프로필) 수정 요청 DTO
public record UserProfileUpdateRequest(

        @Schema(description = "수정할 프로필 이미지 URL", example = "https://new-image-url.com/profile.jpg")
        @NotBlank(message = "수정할 프로필 이미지 주소는 필수입니다.")
        String imageUrl
) {}