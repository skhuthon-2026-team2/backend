package com.project.app.user.api.dto;

import com.project.app.user.domain.User;
import com.project.app.club.domain.ClubMember;
import java.time.LocalDateTime;
import java.util.List;

public record UserProfileResponse(
        Long userId,
        Long kakaoId,
        String imageUrl,
        String nickname,
        LocalDateTime createdAt,
        List<ClubProfileDto> myClubs // 마이페이지 조회 시에만 채워질 리스트
) {
    // 💡 1. [기존 유지용] 일반 유저 프로필 조회 시 사용 (동아리 리스트를 빈 값으로 처리)
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getKakaoId(),
                user.getImageUrl(),
                user.getNickname(),
                user.getCreatedAt(),
                List.of() // 빈 리스트 제공하여 하위 호환성 유지
        );
    }

    // 💡 2. [마이페이지 통합용] 가입한 동아리 목록까지 묶어서 반환할 때 사용
    public static UserProfileResponse of(User user, List<ClubMember> clubMembers) {
        List<ClubProfileDto> clubProfileDtos = clubMembers.stream()
                .map(ClubProfileDto::from)
                .toList();

        return new UserProfileResponse(
                user.getId(),
                user.getKakaoId(),
                user.getImageUrl(),
                user.getNickname(),
                user.getCreatedAt(),
                clubProfileDtos
        );
    }

    // 내부 서브 DTO
    public record ClubProfileDto(
            Long clubId,
            String clubName,
            Long clubMemberId,
            String clubNickname,
            String clubRole
    ) {
        public static ClubProfileDto from(ClubMember clubMember) {
            return new ClubProfileDto(
                    clubMember.getClub().getId(),
                    clubMember.getClub().getName(),
                    clubMember.getId(),
                    clubMember.getNickname(),
                    clubMember.getRole().name()
            );
        }
    }
}