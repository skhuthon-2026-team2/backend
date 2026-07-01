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
     * 동아리 생성 API (더미)
     */
    @PostMapping
    @Operation(summary = "동아리 생성", description = "새로운 동아리를 생성합니다. (더미)")
    public ApiResTemplate<Void> createClub(@RequestBody @Valid ClubCreateRequest clubCreateRequest) {
        // 💡 프론트엔드 테스트 환경을 보장하기 위해 실제 서비스 호출은 잠시 막아둡니다.
        clubService.createClub(clubCreateRequest);
        return ApiResTemplate.successWithNoContent(SuccessCode.CLUB_SAVE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_SAVE_SUCCESS.getMessage());
    }

    /**
     * 동아리 상세 조회 API (더미)
     */
    @GetMapping("/{clubId}")
    @Operation(summary = "동아리 상세 조회", description = "동아리 ID를 통해 세부 정보를 조회합니다. (더미)")
    public ApiResTemplate<ClubInfoResponse> getClub(@PathVariable Long clubId) {
        ClubInfoResponse response = clubService.getClub(clubId);

        // 🛠️ 아직 ClubInfoResponse DTO 파일의 내부 구조를 받지 못해 임시로 null을 반환합니다.
        // 나중에 해당 DTO에 필드가 채워지면 가짜 생성자로 조립해 내려줄 수 있습니다.
        // ClubInfoResponse dummyResponse = null;
        //return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), SuccessCode.GET_SUCCESS.getMessage(), dummyResponse);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), SuccessCode.GET_SUCCESS.getMessage(), response);
    }

    /**
     * 동아리 정보 수정 API (더미)
     */
    @PutMapping("/{clubId}")
    @Operation(summary = "동아리 정보 수정", description = "동아리의 이름, 설명, 이미지, 최대 정원을 수정합니다. (더미)")
    public ApiResTemplate<ClubInfoResponse> updateClub(
            @PathVariable Long clubId,
            @RequestBody @Valid ClubUpdateRequest clubUpdateRequest) {

        ClubInfoResponse response = clubService.updateClub(clubId, clubUpdateRequest);

        // 수정 완료 후에도 일단 안전하게 null 혹은 껍데기를 리턴하여 프론트 통신을 성공시킵니다.
        // ClubInfoResponse dummyResponse = null;
        //return ApiResTemplate.success(SuccessCode.CLUB_UPDATE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_UPDATE_SUCCESS.getMessage(), dummyResponse);

        return ApiResTemplate.success(SuccessCode.CLUB_UPDATE_SUCCESS.getHttpStatusCode(), SuccessCode.CLUB_UPDATE_SUCCESS.getMessage(), response);
    }
}