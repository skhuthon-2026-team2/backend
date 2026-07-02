package com.project.app.club.api.dto;

import com.project.app.club.domain.Club;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "동아리 상세 조회 응답 DTO")
public record ClubInfoResponse(
        @Schema(description = "동아리 식별 ID", example = "10")
        Long clubId,

        @Schema(description = "동아리 이름", example = "모담 동아리")
        String clubName,

        @Schema(description = "동아리 소개글", example = "함께 성장하는 모담 동아리입니다.")
        String description,

        @Schema(description = "동아리 초대 코드", example = "X8Z2K9L")
        String inviteCode,

        @Schema(description = "동아리 대표 이미지 URL", example = "https://dummyimage.com/club.jpg")
        String clubImageUrl,

        @Schema(description = "동아리 최대 정원", example = "50")
        Integer maxMembers,

        @Schema(description = "현재 등록된 멤버 수", example = "12")
        Long currentMembers,

        @Schema(description = "동아리 총 게시글 수", example = "45")
        Long totalPostCount
) {
    public static ClubInfoResponse of(Club club, Long currentMembers, Long totalPostCount) {
        return new ClubInfoResponse(
                club.getId(),
                club.getName(),
                club.getDescription(),
                club.getInviteCode(),
                club.getImageUrl(),
                club.getMaxMembers(),
                currentMembers,
                totalPostCount
        );
    }
}