package com.project.app.ai.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AiKeywordResponse {
    private boolean success;
    private List<String> tags;
    private String image_description;
    private List<String> image_urls; // 💡 AI 서버가 반환하는 이미지 주소 리스트 추가
}