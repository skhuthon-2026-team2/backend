package com.project.app.club.domain;

import com.project.app.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_members")
@Getter
@NoArgsConstructor
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 동아리의 멤버인지 연결 (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    // 어떤 유저인지 연결 (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING) // DB에 문자열("OWNER", "MEMBER")로 저장
    @Column(nullable = false)
    private ClubRole role;

    // 생성한 날짜 저장
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    @Builder
    public ClubMember(Club club, User user, String nickname, String profileImage, ClubRole role, LocalDateTime createdAt) {
        this.club = club;
        this.user = user;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
        // 생성값이 없으면 현재 시간으로
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }

    // 🔥 3번 요구사항: 동아리 내 프로필(닉네임) 수정 메서드
    public void updateProfile(String nickname) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
    }

}