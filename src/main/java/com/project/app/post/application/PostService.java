package com.project.app.post.application;

import com.project.app.club.domain.ClubMember;
import com.project.app.club.domain.ClubMemberRepository; // 가입 확인용 리포지토리
import com.project.app.common.response.code.ErrorCode;
import com.project.app.post.api.dto.PostCreateRequest;
import com.project.app.post.api.dto.PostDetailResponse;
import com.project.app.post.domain.Post;
import com.project.app.post.domain.PostRepository;
import com.project.app.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final ClubMemberRepository clubMemberRepository; // 의존성 추가

    /**
     * 1. 피드 생성
     */
    @Transactional
    public void createPost(Long clubId, Long userId, PostCreateRequest requestDto) {
        // 해당 동아리에 소속된 유저 멤버 정보가 유효한지 검증
        ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_MEMBER_NOT_FOUND, "해당 동아리의 소속 회원이 아닙니다."));

        Post post = Post.builder()
                .clubMember(clubMember) // 수정한 단방향 연관계 주입
                .title(requestDto.title())
                .content(requestDto.content())
                .images(requestDto.imageUrls())
                .activityDate(requestDto.activityDate())
                .build();

        postRepository.save(post);
    }

    /**
     * 2. 피드 수정
     */
    @Transactional
    public void updatePost(Long postId, Long userId, PostCreateRequest requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_FEED, ErrorCode.POST_NOT_FOUND_FEED.getMessage()));

        // 작성자 본인 검증 로직 변경 (ClubMember 객체를 타고 들어가 확인)
        if (!post.getClubMember().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, ErrorCode.HANDLE_ACCESS_DENIED.getMessage());
        }

        // role에 따라서 권한 부여

        post.updatePost(
                requestDto.title(),
                requestDto.content(),
                requestDto.imageUrls(),
                requestDto.activityDate()
        );
    }

    /**
     * 3. 특정 동아리 내 전체 피드 목록 조회
     */
    public Page<PostDetailResponse> getClubPosts(Long clubId, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByClubMemberClubId(clubId, pageable);
        return posts.map(PostDetailResponse::from);
    }

    /**
     * 4. 특정 유저 ID별 피드 목록 조회 (마이페이지 로그인 ID 타겟팅)
     */
    public Page<PostDetailResponse> getUserPosts(Long userId, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByClubMemberUserId(userId, pageable);
        return posts.map(PostDetailResponse::from);
    }

    /**
     * 5. 피드 상세 단건 조회
     */
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_FEED, ErrorCode.POST_NOT_FOUND_FEED.getMessage()));
        return PostDetailResponse.from(post);
    }

    /**
     * 6. 피드 삭제
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_FEED, ErrorCode.POST_NOT_FOUND_FEED.getMessage()));

        // 작성자 권한 체크
        if (!post.getClubMember().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, "삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}