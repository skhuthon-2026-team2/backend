package com.project.app.club.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "clubs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 빈 상자(기본 생성자) 자동 생성
@AllArgsConstructor // 빌더를 쓰기 위해 모든 필드를 모은 생성자 자동 생성
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 동아리 이름
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false) // 동아리 대표 이미지
    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @Column(nullable = false)
    private Integer maxMembers;




    // 운영자가 동아리 관리 페이지에서 수정을 쉽게 하기 위한 메서드 (제미나이가 추천해줘서 추가했습니다)
    public void updateClubSettings(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
}