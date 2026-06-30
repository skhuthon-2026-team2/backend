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

    // 사용자가 지정한 메시지를 리턴
    public BusinessException(ErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
    }
}