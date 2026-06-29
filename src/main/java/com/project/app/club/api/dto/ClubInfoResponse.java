package com.project.app.club.api.dto;

import com.project.app.club.domain.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubInfoResponse {
    // 서버로 가져갈때 아래의 이름으로 나가짐.
    private Long clubId;
    private String clubName;
    private String description;     // 동아리 소개글
    private String inviteCode;      // 자동 생성된 고유 초대 코드
    private String clubImageUrl;    // 대표 이미지 URL
    private Integer maxMembers; // 최대 가입 인원 제한 숫자

    // 엔티티를 DTO로 변환하는 생성자
    public ClubInfoResponse(Club club) {
        this.clubId = club.getId();
        this.clubName = club.getName();
        this.clubImageUrl = club.getImageUrl(); // <- 엔티티의 imageUrl을 여기에 넣는다.
        this.description = club.getDescription();
        this.maxMembers = club.getMaxMembers();
        this.inviteCode = club.getInviteCode();
    }

}
