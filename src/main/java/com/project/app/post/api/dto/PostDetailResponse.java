package com.project.app.post.api.dto;

import com.project.app.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "피드(게시글) 상세 조회 응답 DTO")
public record PostDetailResponse(

        @Schema(description = "피드 식별 ID", example = "1")
        Long postId,

        @Schema(description = "동아리 식별 ID", example = "10")
        Long clubId,

        @Schema(description = "작성자 유저 식별 ID", example = "100")
        Long userId,

        @Schema(description = "작성자 동아리 전용 닉네임", example = "모담프론트")
        String writerNickname,

        @Schema(description = "작성자 동아리 전용 프로필 이미지", example = "https://dummyimage.com/profile.jpg")
        String writerProfileImage,

        @Schema(description = "피드 제목", example = "즐거운 동아리 첫 모임!")
        String title,

        @Schema(description = "피드 내용", example = "다들 반가웠어요! 다음에도 또 봐요~")
        String content,

        @Schema(description = "업로드한 활동 이미지 URL",
                examples = {"https://dummyimage.com/post1.jpg", "https://dummyimage.com/post2.jpg"}
        )
        List<String> imageUrls,

        @Schema(description = "실제 활동 날짜", example = "2023-11-20")
        LocalDate activityDate,

        @Schema(description = "게시글 작성 일시", example = "2023-11-20T18:30:00")
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
                post.getImages(),
                post.getActivityDate(),
                post.getCreatedAt()
        );
    }
}