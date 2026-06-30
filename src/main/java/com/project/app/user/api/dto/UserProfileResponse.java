package com.project.app.user.api.dto;

import com.project.app.user.domain.User;

// 내 프로필 정보 조회 반환할 dto
public record UserProfileResponse(
        Long userId,
        Long kakaoId,
        String imageUrl
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(user.getId(), user.getKakaoId(), user.getImageUrl());
    }
}