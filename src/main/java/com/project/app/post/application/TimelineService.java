package com.project.app.post.application;

import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubRepository;
import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.post.api.dto.TimelineCreateRequest;
import com.project.app.post.api.dto.TimelineDetailResponse;
import com.project.app.post.api.dto.TimelineFeedResponse;
import com.project.app.post.api.dto.TimelineListResponse;
import com.project.app.post.domain.*;
import com.project.app.user.domain.User;
import com.project.app.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final TimelinePostRepository timelinePostRepository;
    private final ClubRepository clubRepository;
    private final PostRepository postRepository; // 게시글 조회를 위해 주입
    private final UserRepository userRepository;

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
    public List<TimelineFeedResponse> getFeedsByDate(Long clubId, LocalDate startDate, LocalDate endDate) {
        validateClubExists(clubId);

        // 하루 시작 시점과 끝 시점을 이용해 해당 날짜의 전체 게시글 검색
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Post> posts = postRepository.findAllByClubIdAndCreatedAtBetween(clubId, startDateTime, endDateTime);
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(TimelineFeedResponse::from)
                .toList();
    }

    /**
     * 2-2. 타임라인 생성 및 저장 (+ 선택한 게시글들 연관 매핑)
     */
    @Transactional
    public void createTimeline(Long clubId, Long userId,TimelineCreateRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, "동아리를 찾을 수 없습니다."));

        // 작성자 유저 조회 (Request DTO나 SecurityContextHolder 등에서 로그인된 userId를 가져온다고 가정)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_MEMBER_NOT_FOUND,ErrorCode.CLUB_NOT_FOUND_EXCEPTION.getMessage()));

        // 사용자가 선택한 이미지가 없으면 기본 이미지 제공
        String repUrl = (request.representativeImageUrl() != null && !request.representativeImageUrl().isBlank())
                ? request.representativeImageUrl()
                : "default_timeline_image_url";

        // 사진 개수당 2초로 자동 계산 (게시글 리스트의 크기 * 2)
        int calculatedDuration = request.posts().size() * 2;

        // 1. 타임라인 기본 객체 생성 및 저장
        Timeline timeline = Timeline.builder()
                .club(club)
                .user(user)
                .title(request.title())
                .duration(calculatedDuration)
                .representativeImageUrl(repUrl)
                .build();

        Timeline savedTimeline = timelineRepository.save(timeline);

        // 2. DTO에 담긴 게시글 ID 목록만 추출
        List<Long> postIds = request.posts().stream()
                .map(TimelineCreateRequest.PostOrderRequest::postId)
                .toList();

        // 3. 최적화 쿼리로 게시글 데이터 일괄 조회 후 빠른 매핑을 위해 Map(ID -> Post)으로 변환
        List<Post> selectedPosts = postRepository.findAllByIdsWithClub(postIds);
        Map<Long, Post> postMap = selectedPosts.stream()
                .collect(Collectors.toMap(Post::getId, post -> post));

        // 자식 엔티티(TimelinePost)들을 임시로 모아둘 바구니(List)를 생성합니다.
        List<TimelinePost> timelinePostsToSave = new ArrayList<>();

        // 4. 프론트엔드가 요청한 게시글 순서(PostOrderRequest) 순회하며 매핑 객체 조립
        for (TimelineCreateRequest.PostOrderRequest postOrder : request.posts()) {
            Post post = postMap.get(postOrder.postId());
            if (post == null) {
                throw new BusinessException(ErrorCode.POST_NOT_FOUND_FEED, "존재하지 않는 게시글이 포함되어 있습니다.");
            }

            // 동아리 ID 검증
            Long postClubId = post.getClubMember().getClub().getId();
            if (!postClubId.equals(clubId)) {
                throw new BusinessException(ErrorCode.INVALID_POST_CLUB_EXCEPTION, ErrorCode.INVALID_INVITE_CODE.getMessage());
            }

            // 💡 게시글과 순서(sequence) 정보를 묶어서 타임라인에 추가
            TimelinePost timelinePost = new TimelinePost(post, postOrder.sequence());
            // ⭐ 확실하게 ID가 생성된 부모(savedTimeline)를 자식에게 명시적으로 주입! (FK null 방지)
            timelinePost.assignTimeline(savedTimeline);
            // 생성된 자식을 리스트에 담음
            timeline.addTimelinePost(timelinePost);
        }

        // 5. 자식 테이블을 명시적으로 직접 일괄 저장합니다.
        timelinePostRepository.saveAll(timelinePostsToSave);
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

    /**
     * 1. 내 타임라인 목록 확인
     */
    public Page<TimelineListResponse> getMyTimelines(Long userId, Pageable pageable) {
       // validateClubExists(clubId);

        Page<Timeline> timelines = timelineRepository.findAllByUser_Id(userId, pageable);
        return timelines.map(TimelineListResponse::from);
    }

    // 존재하는 동아리인지
    private void validateClubExists(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, "존재하지 않는 동아리입니다.");
        }
    }
}