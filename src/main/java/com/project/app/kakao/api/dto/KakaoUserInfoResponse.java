package com.project.app.kakao.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

    // 카카오가 발급하는 유저 고유 식별자 번호
    @JsonProperty("id")
    private Long id;

    // 이메일이나 프로필 정보를 더 가져오고 싶다면 여기에 kakao_account 구조를 추가하면 됩니다.
    // 현재는 고유 ID(kakao_id)만 필수로 쓰므로 이대로 간결하게 시작합니다.
}