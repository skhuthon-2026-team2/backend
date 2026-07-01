package com.project.app.club.api.dto;

import com.project.app.club.domain.ClubMember;
import com.project.app.club.domain.ClubRole;

public record ClubMemberResponse(
        Long clubMemberId,
        Long userId,
        String nickname,
        String profileImage,
        ClubRole role
) {
    public static ClubMemberResponse from(ClubMember clubMember) {
        return new ClubMemberResponse(
                clubMember.getId(),
                clubMember.getUser().getId(),
                clubMember.getNickname(),
                clubMember.getProfileImage(),
                clubMember.getRole()
        );
    }
}