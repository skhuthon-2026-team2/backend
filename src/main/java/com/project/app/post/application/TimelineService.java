package com.project.app.post.application;

import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubRepository;
import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.post.api.dto.TimelineCreateRequest;
import com.project.app.post.api.dto.TimelineDetailResponse;
import com.project.app.post.api.dto.TimelineFeedResponse;
import com.project.app.post.api.dto.TimelineListResponse;
import com.project.app.post.domain.Post;
import com.project.app.post.domain.PostRepository;
import com.project.app.post.domain.Timeline;
import com.project.app.post.domain.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final ClubRepository clubRepository;
    private final PostRepository postRepository; // 게시글 조회를 위해 주입

    /**
     * 1. 동아리 내 타임라인 목록 확인
     */
    public Page<TimelineListResponse> getTimelines(Long clubId, Pageable pageable) {
        validateClubExists(clubId);

        Page<Timeline> timelines = timelineRepository.findAllByClubId(clubId, pageable);
        return timelines.map(TimelineListResponse::from);
    }

    /**
     * 2-1. 특정 날짜에 생성된 게시글 목록 조회 (체크박스 노출용)
     */
    public List<TimelineFeedResponse> getFeedsByDate(Long clubId, LocalDate date) {
        validateClubExists(clubId);

        // 하루 시작 시점과 끝 시점을 이용해 해당 날짜의 전체 게시글 검색
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Post> posts = postRepository.findAllByClubIdAndCreatedAtBetween(clubId, startOfDay, endOfDay);

        return posts.stream()
                .map(TimelineFeedResponse::from)
                .toList();
    }

    /**
     * 2-2. 타임라인 생성 및 저장 (+ 선택한 게시글들 연관 매핑)
     */
    @Transactional
    public void createTimeline(Long clubId, TimelineCreateRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, "동아리를 찾을 수 없습니다."));

        // 1. 타임라인 기본 객체 생성 및 저장
        Timeline timeline = Timeline.builder()
                .club(club)
                .title(request.title())
                .build();

        // 2. 선택된 게시글 ID 목록으로 게시글들을 조회하여 타임라인에 주입
        List<Post> selectedPosts = postRepository.findAllById(request.postIds());
        for (Post post : selectedPosts) {
            timeline.addPost(post);
        }

        timelineRepository.save(timeline);
    }

    /**
     * 3. 제작 완료된 타임라인 크게 불러오기 (상세 조회)
     */
    public TimelineDetailResponse getTimelineDetail(Long clubId, Long timelineId) {
        validateClubExists(clubId);

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TIMELINE_NOT_FOUND, ErrorCode.TIMELINE_NOT_FOUND.getMessage()));

        if (!timeline.getClub().getId().equals(clubId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS, ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
        }

        return TimelineDetailResponse.from(timeline);
    }

    private void validateClubExists(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, "존재하지 않는 동아리입니다.");
        }
    }
}