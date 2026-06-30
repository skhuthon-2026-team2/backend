package com.project.app.post.domain;

import com.project.app.club.domain.Club;
import com.project.app.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 동아리에 쓴 피드인가? (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    // 누가 쓴 피드인가? (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true, length = 2000) // 활동 내용 (Null 허용)
    private String content;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDate activityDate; // 캘린더 연/월/일 날짜 저장

    // 생성한 날짜 저장
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 🔥 피드 수정용 메서드 (Dirty Checking)
    public void updatePost(String title, String content, String imageUrl, LocalDate activityDate) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.activityDate = activityDate;
    }
}