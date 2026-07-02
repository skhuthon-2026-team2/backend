package com.project.app.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timeline_posts")
@Getter
@NoArgsConstructor
public class TimelinePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeline_id", nullable = false)
    private Timeline timeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private int sequence; // 슬라이드 재생 순서 (1, 2, 3...)

    public TimelinePost(Post post, int sequence) {
        this.post = post;
        this.sequence = sequence;
    }

    public void assignTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}