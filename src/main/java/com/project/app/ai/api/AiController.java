package com.project.app.ai.api;

import com.project.app.ai.api.dto.AiKeywordResponse;
import com.project.app.ai.application.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
// 💡 Swagger 상단에 노출될 그룹 이름과 대분류 설명을 설정합니다.
@Tag(name = "Activity & AI", description = "활동 기록 분석 및 인스타 감성 해시태그 추출 API")
public class AiController {

    private final AiService aiService;

    /**
     * 프론트엔드에서 사진과 정보를 받아 AI 서버로 분석을 요청하는 API
     */
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // 💡 API의 제목(summary)과 상세 설명(description)을 이쁘게 달아줍니다.
    @Operation(
            summary = "활동 기록 이미지 분석 및 키워드 생성",
            description = "동아리 활동 사진과 추가 정보(날짜, 장소, 시간)를 입력받아 AI 서버를 통해 해시태그 및 피드 설명을 자동 생성합니다."
    )
    public ResponseEntity<AiKeywordResponse> analyzeImage(
            @Parameter(description = "분석할 동아리 활동 사진 파일 (필수)", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "활동이 일어난 날짜 (예: 2026-07-02)")
            @RequestParam(value = "date", required = false) String date,

            @Parameter(description = "활동 장소 (예: 한강공원)")
            @RequestParam(value = "place", required = false) String place,

            @Parameter(description = "활동 시간 (예: 14:30)")
            @RequestParam(value = "time", required = false) String time
    ) {
        // AiService를 호출하여 AI 서버 통신 후 결과 받아오기
        AiKeywordResponse response = aiService.getAiKeywords(file, date, place, time);

        // 프론트엔드에게 성공적으로 결과 반환
        return ResponseEntity.ok(response);
    }
}