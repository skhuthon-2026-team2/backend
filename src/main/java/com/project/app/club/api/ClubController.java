package com.project.app.club.api;

import com.project.app.club.api.dto.ClubCreateRequest;
import com.project.app.club.api.dto.ClubInfoResponse;
import com.project.app.club.application.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Club", description = "동아리 관리 API")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @Operation(summary = "동아리 생성", description = "새로운 동아리를 생성하고 고유한 무작위 초대 코드를 발급합니다.")
    @PostMapping
    public ResponseEntity<ClubInfoResponse> createClub(@RequestBody ClubCreateRequest request) {
        // 서비스 단을 거쳐 생성 완료된 ClubInfoResponse 객체를 받아옴
        ClubInfoResponse response = clubService.createClub(request);

        // 프론트엔드에 200 OK 상태 코드와 함께 생성된 동아리 정보(응답 객체)를 반환
        return ResponseEntity.ok(response);
    }
}
