package com.project.app.club.application;

import com.project.app.club.api.dto.ClubJoinRequest;
import com.project.app.club.api.dto.ClubMemberProfileUpdateRequest;
import com.project.app.club.api.dto.ClubMemberResponse;
import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubMember;
import com.project.app.club.domain.ClubMemberRepository;
import com.project.app.club.domain.ClubRole;
import com.project.app.club.domain.ClubRepository;
import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.user.domain.User;
import com.project.app.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubManagementService {

    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    /**
     * 1. 특정 동아리의 멤버 목록 조회 (페이징)
     */
    public Page<ClubMemberResponse> getClubMembers(Long clubId, Pageable pageable) {
        // 동아리 존재 여부 먼저 확인
        if (!clubRepository.existsById(clubId)) {
            throw new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, ErrorCode.CLUB_NOT_FOUND_EXCEPTION.getMessage() + clubId);
        }

        Page<ClubMember> members = clubMemberRepository.findAllByClubId(clubId, pageable);
        return members.map(ClubMemberResponse::from);
    }

    /**
     * 2. 동아리 가입 (초대코드 검증 및 가입 완료)
     */
    @Transactional
    public void joinClub(Long clubId, Long userId, ClubJoinRequest requestDto) {
        // 중복 가입 확인
        if (clubMemberRepository.existsByClubIdAndUserId(clubId, userId)) {
            throw new BusinessException(ErrorCode.CLUB_MEMBER_ALREADY_EXISTS, ErrorCode.CLUB_MEMBER_ALREADY_EXISTS.getMessage());
        }

        // 대상 동아리와 유저 엔티티 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_NOT_FOUND_EXCEPTION, ErrorCode.CLUB_NOT_FOUND_EXCEPTION.getMessage() + clubId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND_EXCEPTION, "존재하지 않는 유저입니다."));

        // 입력받은 초대코드가 동아리의 초대코드와 일치하는지 검증
        if (!club.getInviteCode().equals(requestDto.inviteCode())) {
            throw new BusinessException(ErrorCode.INVALID_INVITE_CODE, ErrorCode.INVALID_INVITE_CODE.getMessage());
        }

        // 유저 고유의 기본 프로필 이미지나 별명을 매핑하여 멤버 생성 (동아리별 첫 멤버면 OWNER, 아니면 MEMBER)
        long memberCount = clubMemberRepository.countByClubId(clubId);
        ClubRole assignedRole = (memberCount == 0) ? ClubRole.OWNER : ClubRole.MEMBER;

        // 동아리 가입 사용자가 정한 가입 인원 넘으면 가입 막기
        if (memberCount >= club.getMaxMembers()) {
            throw new BusinessException(
                    ErrorCode.CLUB_MAX_MEMBERS_EXCEEDED, // 에러코드 예시
                    "동아리 정원이 초과되어 더 이상 가입할 수 없습니다. (최대 인원: " + club.getMaxMembers() + "명)"
            );
        }

        ClubMember clubMember = ClubMember.builder()
                .club(club)
                .user(user)
                .nickname(requestDto.getNickname()) // 가입 초기 시 유저 원래 닉네임 사용 권장
                .profileImage(requestDto.getProfileImage() != null ? requestDto.profileImage() : "default_image_url") // 유저 프로필 이미지 활용
                .role(assignedRole)
                .build();

        clubMemberRepository.save(clubMember);
    }

    /**
     * 3. 동아리 내 내 활동 별명(프로필) 수정
     */
    @Transactional
    public void updateMyProfile(Long clubId, Long userId, ClubMemberProfileUpdateRequest requestDto) {
        // 단건 조회
        ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLUB_MEMBER_NOT_FOUND, ErrorCode.CLUB_MEMBER_NOT_FOUND.getMessage()));

        // 🔥 수정 포인트: 엔티티 스펙에 맞춰 nickname 인자 하나만 넘기도록 수정!
        clubMember.updateProfile(requestDto.nickname());
    }
}