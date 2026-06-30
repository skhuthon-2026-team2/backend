package com.project.app.kakao.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    @JsonProperty("id")
    private Long id;                  // 카카오 고유 ID

    private String profileImageUrl;   // 카카오 프로필 이미지 URL

    // 만약 카카오에서 데이터 받을 때 필드명이 다르다면 @JsonProperty 등을 사용해야 할 수 있습니다.
}