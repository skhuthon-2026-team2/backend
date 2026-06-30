package com.project.app.post.api.dto;

import com.project.app.post.domain.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;

//
public record PostDetailResponse(
        Long postId,
        Long clubId,
        Long userId,
        String writerNickname,
        String title,
        String content,
        String imageUrl,
        LocalDate activityDate,
        LocalDateTime createdAt
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getId(),
                post.getClub().getId(),
                post.getUser().getId(),
                post.getUser().getNickname(), // User 엔티티에 닉네임 필드가 있다고 가정
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getActivityDate(),
                post.getCreatedAt()
        );
    }
}