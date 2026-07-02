package com.project.app.user.api;


import com.project.app.common.response.ApiResTemplate;
import com.project.app.common.response.code.SuccessCode;
import com.project.app.user.api.dto.MyFeedListResponse;
import com.project.app.user.api.dto.UserProfileResponse;
import com.project.app.user.api.dto.UserProfileUpdateRequest;
import com.project.app.user.application.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{userId}")
    @Operation(summary = "유저 정보 조회", description = "특정 유저의 프로필 정보 조회")
    public ApiResTemplate<UserProfileResponse> getUserProfile(@PathVariable Long userId) {

        UserProfileResponse response = userService.getUserProfile(userId);
        return ApiResTemplate.success(
                SuccessCode.USER_LOAD_SUCCESS.getHttpStatusCode(), // 💡 가용하시는 회원 조회 성공코드로 매핑하세요
                SuccessCode.USER_LOAD_SUCCESS.getMessage(),
                response
        );
    }

    @PatchMapping("/{userId}/profile")
    @Operation(summary = "유저 프로필 수정", description = "유저의 프로필 이미지를 수정합니다.")
    public ApiResTemplate<UserProfileResponse> updateProfile(
            @PathVariable Long userId,
            @RequestBody @Valid UserProfileUpdateRequest requestDto) {

        UserProfileResponse response = userService.updateUserProfile(userId, requestDto);
        return ApiResTemplate.success(
                SuccessCode.USER_UPDATE_SUCCESS.getHttpStatusCode(), // 💡 프로젝트의 성공코드로 매핑하세요
                SuccessCode.USER_UPDATE_SUCCESS.getMessage(),
                response
        );
    }

    @GetMapping("/{userId}/feeds")
    @Operation(summary = "내가 작성한 피드 조회", description = "특정 유저가 작성한 피드 목록을 조회합니다.")
    public ApiResTemplate<MyFeedListResponse> getMyFeeds(@PathVariable Long userId) {

        MyFeedListResponse response = userService.getMyFeeds(userId);
        return ApiResTemplate.success(
                SuccessCode.GET_SUCCESS.getHttpStatusCode(), // 💡 프로젝트의 성공코드로 매핑하세요
                SuccessCode.GET_SUCCESS.getMessage(),
                response
        );
    }
}