package com.project.app.club.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record ClubJoinRequest(
        @NotBlank(message = "초대코드는 필수 입력 사항입니다.")
        String inviteCode,

        @Getter
        @NotBlank(message = "동아리에서 사용할 별명을 입력해주세요.")
        String nickname,

        @Getter
        String profileImage // 선택 사항 혹은 기본값 처리
) {}