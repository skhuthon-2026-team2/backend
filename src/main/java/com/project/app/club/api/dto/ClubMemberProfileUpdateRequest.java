package com.project.app.club.api.dto;

import jakarta.validation.constraints.NotBlank;

// 동아리 멤버 프로필(별명) 수정 dto
public record ClubMemberProfileUpdateRequest(
        @NotBlank(message = "수정할 별명을 입력해주세요.")
        String nickname
) {}