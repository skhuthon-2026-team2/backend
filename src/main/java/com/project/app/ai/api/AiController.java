package com.project.app.ai.api;

import com.project.app.ai.api.dto.AiKeywordRequest;
import com.project.app.ai.api.dto.AiKeywordResponse;
import com.project.app.ai.application.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "Activity & AI", description = "활동 기록 분석 및 인스타 감성 해시태그 추출 API")
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze")
    @Operation(
            summary = "활동 기록 데이터 분석 및 키워드 생성",
            description = "활동 데이터(제목, 장소, 설명, 날짜, 이미지 URL)를 JSON 바디로 받아 AI 서버 분석 결과를 반환합니다."
    )
    public ResponseEntity<AiKeywordResponse> analyzeImage(@RequestBody AiKeywordRequest request) {
        // 프론트엔드가 보낸 JSON 데이터를 그대로 서비스로 토스
        AiKeywordResponse response = aiService.getAiKeywords(request);
        return ResponseEntity.ok(response);
    }
}