package com.project.app.post.api.dto;

import com.project.app.post.domain.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostDetailResponse(
        Long postId,
        Long clubId,
        Long userId,
        String writerNickname,
        String writerProfileImage, // 동아리 내 프로필 이미지 추가
        String title,
        String content,
        String imageUrl,
        LocalDate activityDate,
        LocalDateTime createdAt
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getClubMember().getClub().getId(),
                post.getClubMember().getUser().getId(),
                post.getClubMember().getNickname(),      // 동아리 전용 활동 별명
                post.getClubMember().getProfileImage(),  // 동아리 전용 이미지
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getActivityDate(),
                post.getCreatedAt()
        );
    }
}