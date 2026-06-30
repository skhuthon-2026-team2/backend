package com.project.app.club.api;

import com.project.app.common.response.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.project.app.common.response.ApiResTemplate;
import com.project.app.club.api.dto.ClubInfoResponse;
import com.project.app.club.api.dto.ClubCreateRequest;
import com.project.app.club.api.dto.ClubUpdateRequest;
import com.project.app.club.application.ClubService;

@Tag(name = "Club", description = "동아리 관련 API")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    /**
     * 동아리 생성 API
     */
    @PostMapping
    @Operation(summary = "동아리 생성", description = "새로운 동아리를 생성합니다.")
    public ApiResTemplate<Void> createClub(@RequestBody @Valid ClubCreateRequest clubCreateRequest) {
        clubService.createClub(clubCreateRequest);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_SAVE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_SAVE_SUCCESS.getMessage());
    }

    /**
     * 동아리 상세 조회 API
     */
    @GetMapping("/{clubId}")
    @Operation(summary = "동아리 상세 조회", description = "동아리 ID를 통해 세부 정보를 조회합니다.")
    public ApiResTemplate<ClubInfoResponse> getClub(@PathVariable Long clubId) {
        ClubInfoResponse response = clubService.getClub(clubId);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(),SuccessCode.GET_SUCCESS.getMessage(), response);
    }

    /**
     * 동아리 정보 수정 API
     */
    @PutMapping("/{clubId}")
    @Operation(summary = "동아리 정보 수정", description = "동아리의 이름, 설명, 이미지, 최대 정원을 수정합니다.")
    public ApiResTemplate<ClubInfoResponse> updateClub(
            @PathVariable Long clubId,
            @RequestBody @Valid ClubUpdateRequest clubUpdateRequest) {

        ClubInfoResponse response = clubService.updateClub(clubId, clubUpdateRequest);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_UPDATE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_UPDATE_SUCCESS.getMessage());
    }
}