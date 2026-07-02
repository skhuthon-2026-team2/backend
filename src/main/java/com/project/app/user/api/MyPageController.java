package com.project.app.user.api;

import com.project.app.club.api.dto.ClubMemberProfileUpdateRequest;
import com.project.app.club.application.ClubManagementService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.post.api.dto.PostDetailResponse;
import com.project.app.post.api.dto.TimelineListResponse;
import com.project.app.post.application.PostService;
import com.project.app.post.application.TimelineService;
import com.project.app.user.api.dto.UserProfileResponse;
import com.project.app.user.api.dto.UserProfileUpdateRequest;
import com.project.app.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "마이페이지", description = "마이페이지 화면 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    // 필요한 서비스를 주입받아 분산 처리합니다.
    private final UserService userService;
    private final ClubManagementService clubManagementService;
    private final PostService postService;
    private final TimelineService timelineService;

    @PatchMapping("/profile")
    @Operation(summary = "나의 기본 프로필 수정", description = "서비스 전체에서 사용하는 회원 본인의 기본 프로필을 수정합니다.")
    public ApiResTemplate<Void> updateProfile(
            @RequestParam("userId") Long userId,
            @RequestBody @Valid UserProfileUpdateRequest request) {
        userService.updateUserProfile(userId, request);
        return ApiResTemplate.successWithNoContent(SuccessCode.USER_UPDATE_SUCCESS.getHttpStatusCode(), "기본 프로필 수정 완료");
    }

    @PatchMapping("/clubs/{clubId}/profile")
    @Operation(summary = "동아리별 프로필 수정", description = "특정 동아리 내에서 사용할 활동 닉네임을 수정합니다.")
    public ApiResTemplate<Void> updateClubProfile(
            @PathVariable("clubId") Long clubId,
            @RequestParam("clubMemberId") Long clubMemberId,
            @RequestBody @Valid ClubMemberProfileUpdateRequest request) {
        clubManagementService.updateMyProfile(clubId, clubMemberId, request);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_MEMBER_UPDATE_SUCCESS.getHttpStatusCode(), "동아리별 프로필 수정 완료");
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "카카오 ID, 유저 ID, 동아리 멤버 ID, 동아리별 닉네임 등의 통합 정보를 조회합니다.")
    public ApiResTemplate<UserProfileResponse> getMyInfo(@RequestParam("userId") Long userId) {
        UserProfileResponse response = userService.getMyIntegrationInfo(userId);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "통합 내 정보 조회 완료", response);
    }

    @GetMapping("/feeds")
    @Operation(summary = "내가 만든 피드 목록 전체 조회", description = "내가 모든 동아리를 통틀어 작성한 피드 목록을 조회합니다.")
    public ApiResTemplate<Page<PostDetailResponse>> getMyPosts(
            @RequestParam("userId") Long userId,
            @ParameterObject Pageable pageable) {
        Page<PostDetailResponse> response = postService.getUserPosts(userId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "내 피드 목록 조회 완료", response);
    }

    @GetMapping("/timelines")
    @Operation(summary = "내 타임라인 조회 (신규)", description = "내가 참여하거나 생성한 타임라인 목록을 조회합니다.")
    public ApiResTemplate<Page<TimelineListResponse>> getMyTimelines(
            @RequestParam("userId") Long userId,
            @ParameterObject Pageable pageable) {
        Page<TimelineListResponse> response = timelineService.getMyTimelines(userId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "내 타임라인 목록 조회 완료", response);
    }
}