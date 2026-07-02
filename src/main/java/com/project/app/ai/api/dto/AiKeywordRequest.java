package com.project.app.ai.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiKeywordRequest {
    private String title;       // 활동 제목
    private String location;    // 장소 (AI서버 스펙에 맞춤)
    private String description; // 설명
    private String date;        // 날짜
    private String image;       // 접근 가능한 이미지 URL
}