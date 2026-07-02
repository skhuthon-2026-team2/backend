package com.project.app.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelinePostRepository extends JpaRepository<TimelinePost, Long> {
}