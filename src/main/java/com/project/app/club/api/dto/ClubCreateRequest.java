package com.project.app.club.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClubCreateRequest(
        @NotBlank(message = "동아리 이름은 필수입니다.")
        String clubName,

        @NotBlank(message = "동아리 설명은 필수입니다.")
        String description,

        @NotBlank(message = "동아리 이미지 URL은 필수입니다.")
        String clubImageUrl,

        @Min(value = 1, message = "동아리 최소 정원은 1명입니다.")
        Integer maxMembers
) {
}
