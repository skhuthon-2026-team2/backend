package com.project.app.common.exception;

import com.project.app.common.response.code.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // 상세 내용 없이 기본 에러 메시지만 사용할 때 (예: 정원 초과 에러)
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 에러 메시지 뒤에 ID나 상세 내용을 붙이고 싶을 때 (예: "해당 사용자가 없습니다. userId = 1")
    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + detail);
        this.errorCode = errorCode;
    }
}