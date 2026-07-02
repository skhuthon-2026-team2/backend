package com.project.app.post.domain;

import com.project.app.club.domain.Club;
import com.project.app.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timelines")
@Getter
@NoArgsConstructor
public class Timeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false)
    private String title; // 타임라인 제목

    @Column(nullable = false)
    private int duration; // 재생 시간 필드(초 단위)

    @Column(name = "representative_image_url")
    private String representativeImageUrl; // 사용자가 선택한 대표 이미지 보관 필드

    // Post와의 직접 연결 대신, 중간 매핑 엔티티(TimelinePost)를 바라봅니다.
    @OneToMany(mappedBy = "timeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimelinePost> timelinePosts = new ArrayList<>();

    @Builder
    public Timeline(Club club, User user,int duration, String title, String representativeImageUrl) {
        this.club = club;
        this.user = user;
        this.title = title;
        this.duration = duration;
        this.representativeImageUrl = representativeImageUrl;
    }

    // 💡 연관관계 편의 메서드 수정 (TimelinePost를 리스트에 담아줍니다)
    public void addTimelinePost(TimelinePost timelinePost) {
        this.timelinePosts.add(timelinePost);
        timelinePost.assignTimeline(this); // 중간 객체에 내 자신(Timeline) 주입
    }
}