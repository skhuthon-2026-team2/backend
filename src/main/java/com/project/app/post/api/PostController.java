package com.project.app.post.api;

import com.project.app.post.api.dto.PostCreateRequest;
import com.project.app.post.api.dto.PostDetailResponse;
import com.project.app.post.application.PostService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "피드(게시글) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/clubs/{clubId}/posts")
    @Operation(summary = "피드 생성", description = "동아리 소속 인증 절차를 거친 후 새로운 피드를 작성합니다. (더미)")
    public ApiResTemplate<Void> createPost(
            @PathVariable("clubId") Long clubId,
            @RequestParam("userId") Long userId,
            @RequestBody @Valid PostCreateRequest requestDto) {

        // 💡 프론트 테스트를 위해 실제 서비스 로직은 잠시 주석 처리합니다.
        // postService.createPost(clubId, userId, requestDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_SAVE_SUCCESS.getHttpStatusCode(), SuccessCode.POST_SAVE_SUCCESS.getMessage());
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "피드 수정", description = "본인이 작성한 피드의 제목, 본문, 일정 날짜 등을 수정합니다. (더미)")
    public ApiResTemplate<Void> updatePost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId,
            @RequestBody @Valid PostCreateRequest requestDto) {

        // postService.updatePost(postId, userId, requestDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_UPDATE_SUCCESS.getHttpStatusCode(), SuccessCode.POST_UPDATE_SUCCESS.getMessage());
    }

    @GetMapping("/clubs/{clubId}/posts")
    @Operation(summary = "동아리별 피드 목록 조회", description = "특정 동아리의 전체 피드 리스트를 최신순으로 페이징 조회합니다. (더미)")
    public ApiResTemplate<Page<PostDetailResponse>> getClubPosts(
            @PathVariable("clubId") Long clubId,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {

        // Page<PostDetailResponse> response = postService.getClubPosts(clubId, pageable);

        // 🛠️ 에러가 나지 않도록 일단 빈 페이지(Empty Page)를 반환합니다.
        Page<PostDetailResponse> dummyResponse = Page.empty(pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "동아리 피드 조회가 완료되었습니다.", dummyResponse);
    }

    @GetMapping("/posts/user")
    @Operation(summary = "유저별 피드 목록 조회", description = "로그인한 유저 본인이 여러 동아리에서 썼던 전체 피드 내역을 한눈에 페이징 조회합니다. (더미)")
    public ApiResTemplate<Page<PostDetailResponse>> getUserPosts(
            @RequestParam("userId") Long userId,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {

        // Page<PostDetailResponse> response = postService.getUserPosts(userId, pageable);

        Page<PostDetailResponse> dummyResponse = Page.empty(pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "유저가 작성한 피드 조회가 완료되었습니다.", dummyResponse);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "피드 상세 조회", description = "피드 식별 ID를 이용하여 상세 본문을 1건 조회합니다. (더미)")
    public ApiResTemplate<PostDetailResponse> getPostDetail(@PathVariable("postId") Long postId) {

        // PostDetailResponse response = postService.getPostDetail(postId);

        // 🛠️ 아직 DTO 구조를 모르므로 null을 반환합니다. 구조가 정해지면 임시 데이터를 넣을 수 있습니다.
        PostDetailResponse dummyResponse = null;
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "피드 상세 조회가 완료되었습니다.", dummyResponse);
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "피드 삭제", description = "작성자 본인 검증 후 피드를 삭제합니다. (더미)")
    public ApiResTemplate<Void> deletePost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId) {

        // postService.deletePost(postId, userId);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_DELETE_SUCCESS.getHttpStatusCode(), SuccessCode.POST_DELETE_SUCCESS.getMessage());
    }
}