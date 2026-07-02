package com.project.app.common.exception;

import com.project.app.common.response.code.ErrorCode;
import com.project.app.common.response.ApiResTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionAdvice {

    /**
     * 1. 우리가 직접 던지는 비즈니스 예외 처리 (BusinessException)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResTemplate<Void>> handleBusinessException(BusinessException e) {
        log.error("BusinessException 발생: {}", e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();

        // 수정한 메시지(기본 메시지 + 추가 상세 내용)를 담아 보냅니다.
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResTemplate.error(errorCode.getHttpStatusCode(), e.getMessage()));
    }

    /**
     * 2. DTO 유효성 검사 실패 예외 처리 (@Valid 조건 통과 못 했을 때)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResTemplate<Void>> handleValidationException(MethodArgumentNotValidException e) {

        // 🔥 1. 발생한 모든 유효성 에러 메시지를 수집해서 쉼표(,)로 연결합니다.
        String bindingMessage = e.getBindingResult().getFieldErrors().stream()
                .map(org.springframework.validation.FieldError::getDefaultMessage)
                .collect(java.util.stream.Collectors.joining(", "));

        // 만약 수집된 메시지가 없다면 기본 문구 세팅
        if (bindingMessage.isBlank()) {
            bindingMessage = "잘못된 입력값입니다.";
        }

        // 🔥 2. 기존의 ErrorCode 메시지를 더하는 로직 대신, 추출한 bindingMessage만 그대로 사용합니다.
        String fullMessage = bindingMessage;
        log.error("ValidationException 발생: {}", fullMessage, e);

        return ResponseEntity
                .status(ErrorCode.VALIDATION_EXCEPTION.getHttpStatus())
                .body(ApiResTemplate.error(ErrorCode.VALIDATION_EXCEPTION.getHttpStatusCode(), fullMessage));
    }

    /**
     * 3. 그 외 시스템 내부에서 예상치 못하게 터지는 최상위 에러 처리 (500 에러 백업)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResTemplate<Void>> handleException(Exception e) {
        log.error("예상치 못한 InternalServerError 발생: {}", e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResTemplate.error(errorCode.getHttpStatusCode(), errorCode.getMessage()));
    }
}