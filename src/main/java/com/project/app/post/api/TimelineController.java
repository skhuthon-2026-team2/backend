package com.project.app.post.api;

import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.post.api.dto.TimelineCreateRequest;
import com.project.app.post.api.dto.TimelineDetailResponse;
import com.project.app.post.api.dto.TimelineFeedResponse;
import com.project.app.post.api.dto.TimelineListResponse;
import com.project.app.post.application.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "타임라인", description = "동아리 타임라인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/timelines")
public class TimelineController {

    private final TimelineService timelineService;

    // 1. 타임라인 목록 조회 (페이징)
    @GetMapping
    @Operation(summary = "타임라인 목록 조회", description = "특정 동아리에 만들어진 타임라인 목록을 조회합니다.")
    public ApiResTemplate<Page<TimelineListResponse>> getTimelines(
            @PathVariable("clubId") Long clubId,
            @ParameterObject Pageable pageable) {

        Page<TimelineListResponse> response = timelineService.getTimelines(clubId, pageable);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "타임라인 목록 조회가 완료되었습니다.", response);
    }

    // 특정 날짜에 생성된 게시글(피드) 목록 조회
    @GetMapping("/feeds")
    @Operation(summary = "특정 날짜의 게시글 목록 조회", description = "타임라인 제작 시 특정 날짜에 작성된 게시글(사진, 제목, 날짜 포함)들을 조회합니다.")
    public ApiResTemplate<List<TimelineFeedResponse>> getFeedsByDate(
            @PathVariable("clubId") Long clubId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TimelineFeedResponse> response = timelineService.getFeedsByDate(clubId, date);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "해당 날짜의 게시글 조회가 완료되었습니다.", response);
    }

    // 타임라인 생성 및 제목/선택된 게시글들 저장
    @PostMapping
    @Operation(summary = "타임라인 최종 생성", description = "타임라인 제목과 선택된 게시글 ID 목록을 받아 타임라인을 생성합니다.")
    public ApiResTemplate<Void> createTimeline(
            @PathVariable("clubId") Long clubId,
            @RequestBody @Valid TimelineCreateRequest request) {

        timelineService.createTimeline(clubId, request);
        return ApiResTemplate.successWithNoContent(SuccessCode.TIMELINE_CRATE_SUCCESS.getHttpStatusCode(), SuccessCode.TIMELINE_CRATE_SUCCESS.getMessage());
    }

    // 3. 타임라인 크게 보기 (상세 조회)
    @GetMapping("/{timelineId}")
    @Operation(summary = "타임라인 상세 조회", description = "제작 완료된 특정 타임라인의 전체 내용(제목 및 묶인 게시글들)을 불러옵니다.")
    public ApiResTemplate<TimelineDetailResponse> getTimelineDetail(
            @PathVariable("clubId") Long clubId,
            @PathVariable("timelineId") Long timelineId) {

        TimelineDetailResponse response = timelineService.getTimelineDetail(clubId, timelineId);
        return ApiResTemplate.success(SuccessCode.GET_SUCCESS.getHttpStatusCode(), "타임라인 상세 조회가 완료되었습니다.", response);
    }
}