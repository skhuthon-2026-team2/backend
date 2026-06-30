package com.project.app.club.api.dto;

import com.project.app.club.domain.Club;

public record ClubInfoResponse(
        Long clubId,
        String clubName,
        String description,
        String inviteCode,
        String clubImageUrl,
        Integer maxMembers
) {
    public static ClubInfoResponse from(Club club) {
        return new ClubInfoResponse(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getInviteCode(),
                club.getImageUrl(),
                club.getMaxMembers()
        );
    }
}