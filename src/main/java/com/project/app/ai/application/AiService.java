package com.project.app.ai.application;

import com.project.app.ai.api.dto.AiKeywordRequest;
import com.project.app.ai.api.dto.AiKeywordResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

    // 💡 application.yml에 등록한 url을 자동으로 주입받습니다.
    @Value("${ai.server.url}")
    private String aiServerUrl;

    /**
     * 유저들의 활동 데이터를 JSON 형태로 AI 서버로 던져주고 결과를 받아옵니다.
     */
    public AiKeywordResponse getAiKeywords(AiKeywordRequest requestBody) {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = aiServerUrl + "/api/v1/generate-keywords";

        // 1. HTTP 헤더 설정 (Multipart가 아닌 APPLICATION_JSON 형식입니다)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 요청 엔티티 생성
        HttpEntity<AiKeywordRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        // 3. AI 서버 호출 및 응답 처리
        try {
            AiKeywordResponse response = restTemplate.postForObject(endpoint, requestEntity, AiKeywordResponse.class);

            if (response != null && response.isSuccess()) {
                return response;
            } else {
                throw new RuntimeException("AI 서버 분석 연동 결과가 올바르지 않습니다.");
            }

        } catch (Exception e) {
            System.err.println("AI 서버 호출 중 에러 발생: " + e.getMessage());
            throw new RuntimeException("AI 서버 통신 실패 (담당자 현수님에게 문의하세요!)", e);
        }
    }
}