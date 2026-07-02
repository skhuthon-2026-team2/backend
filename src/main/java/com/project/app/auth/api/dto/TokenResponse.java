package com.project.app.auth.api.dto;


public record TokenResponse (
        String accessToken,
        String refreshToken,
        Long userId
){
    public static TokenResponse of(String accessToken, String refreshToken, Long userId) {
        return new TokenResponse(accessToken, refreshToken, userId);
    }
}