package com.project.app.common.response; // 경로가 response로 되어있는지 확인!

public record ApiResTemplate<T>(
        int status,
        String message,
        T data
) {
    // 에러 응답용 정적 팩토리 메서드
    public static <T> ApiResTemplate<T> error(int status, String message) {
        return new ApiResTemplate<>(status, message, null);
    }

    // 성공 응답용 정적 팩토리 메서드 (선택 사항)
    public static <T> ApiResTemplate<T> success(int status, String message, T data) {
        return new ApiResTemplate<>(status, message, data);
    }

    // 데이터 없는 성공 응답
    public static <T> ApiResTemplate<T> successWithNoContent(int status, String message) {
        return new ApiResTemplate<>(status, message, null);
    }
}