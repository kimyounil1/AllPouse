package com.perfume.allpouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    // 400 잘못된 요청
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

    // 403 권한 없음
    AUTHORITY_FORBIDDEN(403, "권한이 없습니다."),

    // 404 잘못된 리소스 요청
    SOCIAL_ID_NOT_FOUND(404, "존재하지 않는 SOCIAL ID 입니다."),

    // 409 중복된 리소스 요청
    ALREADY_SAVED_SOCIAL_ID(409, "이미 존재하는 SOCIAL_ID 입니다."),

    // 500 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 에러 입니다.");

    private final int code;
    private final String msg;

}
