package com.project.app.club.api;

import com.project.app.club.api.dto.ClubJoinRequest;

import com.project.app.club.api.dto.ClubMemberProfileUpdateRequest;
import com.project.app.club.api.dto.ClubMemberResponse;
import com.project.app.club.api.dto.ClubUpdateRequest;
import com.project.app.club.application.ClubManagementService;
import com.project.app.club.application.ClubService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.post.api.dto.PostDetailResponse;
import com.project.app.post.application.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버 관리", description = "동아리 멤버 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/management")
public class ClubManagementController {

    private final ClubManagementService clubManagementService;
    private final PostService postService;
    private final ClubService clubService;

    // 동아리 멤버 조회
    @GetMapping("/members")
    @Operation(summary = "동아리 멤버 조회", description = "특정 동아리의 전체 회원 목록을 페이징하여 조회합니다.")
    public ApiResTemplate<Page<ClubMemberResponse>> getClubMembers(
            @PathVariable("clubId") Long clubId,
            @ParameterObject Pageable pageable) {

        Page<ClubMemberResponse> response = clubManagementService.getClubMembers(clubId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "멤버 목록 조회가 완료되었습니다.", response);
    }
    // 2. 동아리 멤버 1명 상세 조회 (clubMemberId 기준)
    @GetMapping("/members/{clubMemberId}")
    @Operation(summary = "동아리 멤버 1명 조회", description = "동아리 멤버 ID를 통해 특정 회원 한 명을 조회합니다.")
    public ApiResTemplate<ClubMemberResponse> getClubMember(
            @PathVariable("clubId") Long clubId,
            @PathVariable("clubMemberId") Long clubMemberId) {

        ClubMemberResponse response = clubManagementService.getClubMember(clubId, clubMemberId);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "멤버 상세 조회가 완료되었습니다.", response);
    }

    // 3. 동아리 멤버 삭제 (추방/탈퇴 - clubMemberId 기준)
    @DeleteMapping("/members/{clubMemberId}")
    @Operation(summary = "동아리 멤버 삭제", description = "특정 동아리에서 해당 멤버를 삭제(탈퇴/추방)합니다.")
    public ApiResTemplate<Void> deleteClubMember(
            @PathVariable("clubId") Long clubId,
            @PathVariable("clubMemberId") Long clubMemberId) {

        clubManagementService.deleteClubMember(clubId, clubMemberId);
        return ApiResTemplate.success(SuccessCode.CLUB_MEMBER_DELETE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_MEMBER_DELETE_SUCCESS.getMessage(), null);
    }

    // 피드 목록 조회
    @GetMapping("/posts")
    @Operation(summary = "피드 목록 조회", description = "관리자가 게시글 검열 및 삭제 처리를 하기 위해 전체 피드를 모아봅니다.")
    public ApiResTemplate<Page<PostDetailResponse>> getManagementPosts(
            @PathVariable("clubId") Long clubId,
            @ParameterObject Pageable pageable) {
        Page<PostDetailResponse> response = postService.getClubPosts(clubId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "관리자용 피드 검열 목록 조회 완료", response);
    }

    // 피드 삭제
    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "피드 삭제 (강제 삭제)", description = "운영진 권한으로 동아리 내 부적절한 게시글을 강제 삭제합니다.")
    public ApiResTemplate<Void> forceDeletePost(
            @PathVariable("clubId") Long clubId,
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long adminUserId) { // 시큐리티 미도입으로 우선 관리자 유저 식별자 파라미터 유지
        // 서비스단에 동아리 ID, 게시글 ID, 요청자(관리자) ID를 모두 넘겨 정밀 검증합니다.
        postService.forceDeletePostByAdmin(clubId, postId, adminUserId);

        return ApiResTemplate.success(SuccessCode.POST_DELETE_SUCCESS.getHttpStatusCode(), "운영진 권한 피드 강제 삭제 완료", null);
    }


    // 동아리 정보 수정
    @PutMapping
    @Operation(summary = "동아리 정보 수정", description = "동아리 최대 정원을 수정합니다.")
    public ApiResTemplate<Void> updateClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody @Valid ClubUpdateRequest request) {
        clubService.updateClub(clubId, request); // 운영자 권한으로
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_UPDATE_SUCCESS.getHttpStatusCode(), "동아리 기본 설정 정보 변경 완료");
    }

    // 내 정보 조회
    @GetMapping("/me/role")
    @Operation(summary = "내 정보 조회 (권한 등급 확인용)", description = "현재 진입한 유저가 OWNER인지 MEMBER인지 화면 요소를 다르게 그리기 위해 등급을 확인합니다.")
    public ApiResTemplate<ClubMemberResponse> getMyRoleInClub(
            @PathVariable("clubId") Long clubId,
            @RequestParam("userId") Long userId) {
        ClubMemberResponse response = clubManagementService.getMyClubProfile(clubId, userId);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "현재 동아리 내 유저 권한 등급 확인 완료", response);
    }



    /**
    // 동아리 가입
    @PostMapping("/join")
    @Operation(summary = "동아리 가입", description = "초대코드를 검증하여 동아리에 신규 멤버로 가입합니다.")
    public ApiResTemplate<Void> joinClub(
            @PathVariable("clubId") Long clubId,
            @RequestParam("userId") Long userId, // 추후 로그인 구현 완료 시 시큐리티 어노테이션으로 변경 권장
            @RequestBody @Valid ClubJoinRequest clubJoinRequest) {

        clubManagementService.joinClub(clubId, userId, clubJoinRequest);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_JOIN_SUCCESS.getHttpStatusCode(), "동아리 가입 성공 및 멤버 추가 완료.");
    }
     **/


}