package com.project.app.club.application;

import com.project.app.club.api.dto.ClubCreateRequest;
import com.project.app.club.api.dto.ClubInfoResponse;
import com.project.app.club.domain.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.club.api.dto.ClubUpdateRequest;
import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

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

        return ClubInfoResponse.from(club);
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
                requestDto.clubName(),      // DTO의 clubName -> 엔티티의 name으로 매핑
                requestDto.description(),
                requestDto.clubImageUrl(),  // DTO의 clubImageUrl -> 엔티티의 imageUrl로 매핑
                requestDto.maxMembers()
        );

        // 4. 🔥 레포지토리를 통해 가장 정확한 실시간 가입 멤버 수를 카운트해옵니다.
        long currentMembers = clubMemberRepository.countByClubId(clubId);

        // 5. 수정된 엔티티와 함께 현재 멤버 수를 DTO에 담아 반환
        return ClubInfoResponse.of(club, currentMembers);
    }

    private void validateUpdateRequest(ClubUpdateRequest dto) {
        if (dto.clubName() == null || dto.clubName().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "동아리 이름은 필수 입력 항목입니다.");
        }
        if (dto.description() == null || dto.description().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "동아리 설명은 필수 입력 항목입니다.");
        }
        if (dto.maxMembers() == null || dto.maxMembers() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "올바른 최대 가입 인원을 입력해 주세요.");
        }
    }
}