package com.project.app.ai.application;

import com.project.app.ai.api.dto.AiKeywordResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;

@Service
public class AiService {

    private final String AI_SERVER_URL = "https://skhuthon-ai-part.onrender.com/api/v1/generate-keywords";

    /**
     * 프론트엔드가 보낸 파일과 메타데이터를 받아 AI 서버로 전달하고 분석 결과를 받아옵니다.
     */
    public AiKeywordResponse getAiKeywords(MultipartFile imageFile, String date, String place, String time) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 요청 멀티파트 바디 데이터 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            // 로컬 경로 하드코딩 대신, 프론트에서 업로드한 이미지 리소스를 그대로 전송에 사용합니다.
            if (imageFile != null && !imageFile.isEmpty()) {
                // 💡 파일을 바이트 배열로 추출하고 파일명을 강제로 세팅해서 전송합니다.
                ByteArrayResource fileResource = new ByteArrayResource(imageFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        // 원본 파일명이 없으면 "upload.jpg"라는 기본 이름을 부여합니다.
                        return imageFile.getOriginalFilename() != null ? imageFile.getOriginalFilename() : "upload.jpg";
                    }
                };
                body.add("files", fileResource);
            } else {
                throw new IllegalArgumentException("분석할 이미지 파일이 누락되었습니다.");
            }

            // 데이터가 빈 값이 아닐 때만 AI 서버 전송 폼에 추가
            if (date != null) body.add("date", date);
            if (place != null) body.add("place", place);
            if (time != null) body.add("time", time);

        } catch (Exception e) {
            throw new RuntimeException("AI 요청 바디 구성 중 오류가 발생했습니다.", e);
        }

        // 2. Multipart 전송을 위한 HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 3. 요청 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 4. AI 서버 호출 및 응답 처리
        try {
            // String 대신 생성한 DTO 클래스로 주입받으면 데이터를 객체처럼 깔끔하게 꺼내 쓸 수 있습니다.
            AiKeywordResponse response = restTemplate.postForObject(AI_SERVER_URL, requestEntity, AiKeywordResponse.class);

            if (response != null && response.isSuccess()) {
                return response;
            } else {
                throw new RuntimeException("AI 서버 분석 연동 결과가 올바르지 않습니다.");
            }

        } catch (Exception e) {
            // 가이드라인에 적힌 400, 500, 503 오류 발생 시 처리 로직
            System.err.println("AI 서버 호출 중 에러 발생: " + e.getMessage());
            throw new RuntimeException("AI 서버 통신 실패 (담당자 현수님에게 문의하세요!)", e);
        }
    }
}