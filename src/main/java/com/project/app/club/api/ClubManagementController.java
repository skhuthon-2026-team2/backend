package com.project.app.club.api;

import com.project.app.club.api.dto.ClubJoinRequest;

import com.project.app.club.api.dto.ClubMemberProfileUpdateRequest;
import com.project.app.club.api.dto.ClubMemberResponse;
import com.project.app.club.application.ClubManagementService;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Club Member", description = "동아리 멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/members")
public class ClubManagementController {

    private final ClubManagementService clubManagementService;

    // 동아리 멤버 조회
    @GetMapping
    @Operation(summary = "동아리 멤버 조회", description = "특정 동아리의 전체 회원 목록을 페이징하여 조회합니다.")
    public ApiResTemplate<Page<ClubMemberResponse>> getClubMembers(
            @PathVariable("clubId") Long clubId,
            Pageable pageable) {

        Page<ClubMemberResponse> response = clubManagementService.getClubMembers(clubId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "멤버 목록 조회가 완료되었습니다.", response);
    }

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

    // 동아리 프로필(별명) 수정
    @PatchMapping("/me")
    @Operation(summary = "동아리 프로필(별명) 수정", description = "동아리 내에서 사용할 본인의 활동 별명을 수정합니다.")
    public ApiResTemplate<Void> updateMyProfile(
            @PathVariable("clubId") Long clubId,
            @RequestParam("userId") Long userId,
            @RequestBody @Valid ClubMemberProfileUpdateRequest clubMemberProfileUpdateRequest) {

        clubManagementService.updateMyProfile(clubId, userId, clubMemberProfileUpdateRequest);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_MEMBER_UPDATE_SUCCESS.getHttpStatusCode(), "동아리 내 활동 별명 수정이 완료되었습니다.");
    }
}