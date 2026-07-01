package com.project.app.common.response.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 모든 필드를 파라미터로 받는 생성자 자동 생성 어노테이션
public enum ErrorCode {

    /**
     * 404 NOT FOUND (찾을 수 없음)
     */
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 사용자가 없습니다. userId = "),
    CLUB_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 동아리가 없습니다. clubId = "),
    POST_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 피드가 없습니다. postId = "),
    POST_NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    CLUB_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동아리의 소속 멤버가 아닙니다."),
    TIMELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 타임라인입니다."),

    /**
     * 400 BAD REQUEST (잘못된 요청)
     */
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "유효성 검사에 실패하였습니다 - "),
    CLUB_MAX_MEMBERS_EXCEEDED_EXCEPTION(HttpStatus.BAD_REQUEST, "동아리 정원이 초과되어 가입할 수 없습니다."),
    ALREADY_JOINED_CLUB_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입했거나 가입 신청 대기 중인 동아리입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    CLUB_MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입한 회원입니다."),
    INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "초대 코드가 일치하지 않습니다."),
    CLUB_MAX_MEMBERS_EXCEEDED(HttpStatus.BAD_REQUEST, "동아리의 정원이 초과되어 더 이상 가입할 수 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "해당 동아리의 타임라인이 아닙니다."),

    /**
     * 401 UNAUTHORIZED (인증 실패)
     */
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    KAKAO_LOGIN_FAILED_EXCEPTION(HttpStatus.UNAUTHORIZED, "카카오 로그인에 실패했습니다."),

    /**
     * 403 FORBIDDEN (권한 없음)
     */
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "해당 기능을 사용할 권한이 없습니다. (작성자 또는 운영자만 가능)"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),

    /**
     * 500 INTERNAL SERVER ERROR (내부 서버 에러)
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러가 발생했습니다");


    private final HttpStatus httpStatus;    // HTTP 상태 코드를 스프Remaining에서 쉽게 작성하기 위한 enum값들의 모임
    private final String message;           // 에러 메세지

    public int getHttpStatusCode() {        // HTTP 상태 코드에서 404와 같은 숫자 값만 반환해 주기 위한 메소드
        return httpStatus.value();
    } // 예) 404, 200, 500 같은 숫자
}