package com.project.app.club.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClubUpdateRequest(

        @Min(value = 1, message = "동아리 최소 정원은 1명입니다.")
        Integer maxMembers
) {
}