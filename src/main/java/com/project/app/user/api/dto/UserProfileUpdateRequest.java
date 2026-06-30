package com.project.app.user.api.dto;

// 유저 프로필 이미지(기본 프로필) 수정
public record UserProfileUpdateRequest(
        String imageUrl
) {}