package com.project.app.club.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubCreateRequest {
    private String name;        // 생성할 동아리 이름
    private String description;
    private String imageUrl;
    private int maxMembers;
}
