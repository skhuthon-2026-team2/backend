package com.project.app.post.domain;

import com.project.app.club.domain.Club;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false)
    private String title; // 타임라인 제목

    // 타임라인에 포함된 게시글 목록 (Cascade 설정으로 함께 관리)
    @OneToMany(mappedBy = "timeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Timeline(Club club, String title) {
        this.club = club;
        this.title = title;
    }

    // 게시글 연관관계 편의 메서드
    public void addPost(Post post) {
        this.posts.add(post);
        post.assignTimeline(this); // Post 엔티티에 timeline 연관관계 추가 필요
    }
}