package com.project.app.club.application;

import com.project.app.club.api.dto.ClubCreateRequest;
import com.project.app.club.api.dto.ClubInfoResponse;
import com.project.app.club.api.dto.MyClubListResponse;
import com.project.app.club.domain.ClubMember;
import com.project.app.club.domain.ClubMemberRepository;
import com.project.app.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.club.api.dto.ClubUpdateRequest;
import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PostRepository postRepository;

    /**
     * 동아리 생성
     */
    @Transactional
    public void createClub(ClubCreateRequest requestDto) {

        // 초대 코드 자동 생성 (초기값 예시)
        String inviteCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Club club = Club.builder()
                .name(requestDto.clubName())
                .description(requestDto.description())
                .imageUrl(requestDto.clubImageUrl())
                .maxMembers(requestDto.maxMembers()) // 정상적으로 요청받은 정수 데이터 주입
                .inviteCode(inviteCode)
                .build();

        clubRepository.save(club);
    }

    /**
     * 동아리 상세 조회
     */
    public ClubInfoResponse getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.CLUB_NOT_FOUND_EXCEPTION,
                        ErrorCode.CLUB_NOT_FOUND_EXCEPTION.getMessage() + clubId));

        // 2. 현재 등록 멤버 수 조회
        Long currentMembers = clubMemberRepository.countByClubId(clubId);

        // 3. 동아리 총 게시글 수 조회
        Long totalPostCount = postRepository.countByClubMember_Club_Id(clubId);
        // 4. DTO 조립하여 반환
        return ClubInfoResponse.of(club, currentMembers, totalPostCount);
    }

    /**
     * 동아리 정보 수정
     */
    @Transactional
    public ClubInfoResponse updateClub(Long clubId, ClubUpdateRequest requestDto) {
        // 1. 수정할 동아리가 존재하는지 확인 (없으면 에러 발생)
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.CLUB_NOT_FOUND_EXCEPTION,
                        ErrorCode.CLUB_NOT_FOUND_EXCEPTION.getMessage() + clubId));

        // 2. 빈칸(Null 또는 공백) 검증 로직 추가
        validateUpdateRequest(requestDto);

        // 3. 엔티티 내부의 updateClubSettings 메서드를 호출하여 상태 변경 (Dirty Checking으로 자동 저장)
        club.updateClubSettings(
                requestDto.maxMembers()
        );

        // 3. 동아리 총 게시글 수 조회
        Long totalPostCount = postRepository.countByClubMember_Club_Id(clubId);

        // 4. 🔥 레포지토리를 통해 가장 정확한 실시간 가입 멤버 수를 카운트해옵니다.
        long currentMembers = clubMemberRepository.countByClubId(clubId);

        // 5. 수정된 엔티티와 함께 현재 멤버 수를 DTO에 담아 반환
        return ClubInfoResponse.of(club, currentMembers, totalPostCount);
    }

    /**
     * 내가 속한 동아리 목록 조회 (메인페이지 신규 기능)
     */
    public List<MyClubListResponse> getMyClubList(Long userId) {
        // 1. 해당 유저가 가입한 모든 동아리 멤버 정보(연결고리) 조회
        List<ClubMember> myClubMembers = clubMemberRepository.findAllByUserId(userId);

        // 2. 가입한 동아리 엔티티들을 추출하여 DTO 형태로 변환
        return myClubMembers.stream()
                .map(clubMember -> {
                    Club club = clubMember.getClub();
                    // 각 동아리별 현재 실시간 총 멤버 수 카운트
                    long currentMembers = clubMemberRepository.countByClubId(club.getId());
                    return MyClubListResponse.of(club, currentMembers);
                })
                .toList();
    }


    private void validateUpdateRequest(ClubUpdateRequest dto) {
        if (dto.maxMembers() == null || dto.maxMembers() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "올바른 최대 가입 인원을 입력해 주세요.");
        }
    }
}