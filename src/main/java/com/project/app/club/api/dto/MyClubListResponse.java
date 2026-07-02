package com.project.app.club.api.dto;

import com.project.app.club.domain.Club;

public record MyClubListResponse(
        Long clubId,
        String clubName,
        String description,
        String clubImageUrl,
        long currentMembers // 카드로 노출할 현재 가입 인원 수
) {
    public static MyClubListResponse of(Club club, long currentMembers) {
        return new MyClubListResponse(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getImageUrl(),
                currentMembers
        );
    }
}