package com.project.app.user.application;

import com.project.app.common.exception.BusinessException;
import com.project.app.common.response.code.ErrorCode;
import com.project.app.user.api.dto.MyFeedListResponse;
import com.project.app.user.api.dto.UserProfileResponse;
import com.project.app.user.api.dto.UserProfileUpdateRequest;
import com.project.app.user.domain.User;
import com.project.app.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 특정 유저 정보 조회
     */
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND_EXCEPTION, "해당 유저를 찾을 수 없습니다. id=" + userId));

        return UserProfileResponse.from(user);
    }

    /**
     * 1. 카카오 로그인 및 자동 가입
     */
    @Transactional
    public UserProfileResponse loginOrRegister(Long kakaoId, String kakaoImageUrl) {
        // DB에 기존 카카오 ID로 가입된 유저가 있는지 확인, 없으면 새로 빌드하여 저장
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .kakaoId(kakaoId)
                                .imageUrl(kakaoImageUrl != null ? kakaoImageUrl : "default_image_url")
                                .build()
                ));

        return UserProfileResponse.from(user);
    }

    /**
     * 2. 프로필 수정 (이미지만 수정 가능)
     */
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest requestDto) {
        // 빈칸 검증
        if (requestDto.imageUrl() == null || requestDto.imageUrl().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "수정할 프로필 이미지 주소는 필수입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND_EXCEPTION, "해당 유저를 찾을 수 없습니다. id=" + userId));

        // Dirty Checking으로 자동 업데이트
        user.updateProfileImage(requestDto.imageUrl());

        return UserProfileResponse.from(user);
    }

    /**
     * 3. 내가 쓴 피드 목록 조회 (우선은 빈 리스트 반환 뼈대)
     */
    public MyFeedListResponse getMyFeeds(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND_EXCEPTION, "해당 유저를 찾을 수 없습니다. id=" + userId));

        // TODO: 나중에 FeedRepository 연동 후 유저가 쓴 피드 리스트를 가공하여 주입
        return new MyFeedListResponse(user.getId(), new ArrayList<>());
    }
}