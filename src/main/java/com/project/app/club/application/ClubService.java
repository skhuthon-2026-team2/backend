package com.project.app.club.application;


import com.project.app.club.api.dto.ClubCreateRequest;
import com.project.app.club.api.dto.ClubInfoResponse;
import com.project.app.club.domain.Club;
import com.project.app.club.domain.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    // 동아리 생성
    @Transactional
    public ClubInfoResponse createClub(ClubCreateRequest request) {
        // 무작위로 코드 생성
        String inviteCode = generateUniqueInviteCode();

        // Request DTO에서 꺼내서 엔티티방에 넣어주기
        Club club = Club.builder()
                .name(request.getName())
                .description(request.getDescription())
                .inviteCode(inviteCode)
                .imageUrl(request.getImageUrl())
                .maxMembers(request.getMaxMembers())
                .build();

        Club savedClub = clubRepository.save(club);

        // 4. [★핵심] 작성하신 생성자를 사용하여 엔티티를 바로 ClubInfoResponse로 변환!
        return new ClubInfoResponse(savedClub);
    }

    // 고유한 초대 코드를 만드는 메서드 (중복 체크)
    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // 6개 코드 생성
        } while (clubRepository.existsByInviteCode(inviteCode)); // 중복 코드가 존재하면 재생성

        return inviteCode;
    }

}
