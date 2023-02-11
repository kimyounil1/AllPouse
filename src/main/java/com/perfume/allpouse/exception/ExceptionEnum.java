package com.perfume.allpouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    // 200 옳은 요청
    SUCCESS_RESULT(200, true, "요청을 성공적으로 완료하였습니다."),

    // 400 잘못된 요청
    INVALID_PARAMETER(400,false , "파라미터 값을 확인해주세요."),
    INVALID_TOKEN(400,false,"유효하지 않은 토큰 입니다."),

    EXPIRED_TOKEN(401,false,"유효기간이 지난 토큰입니다."),

    // 403 권한 없음
    AUTHORITY_FORBIDDEN(403,false , "권한이 없습니다."),

    // 404 잘못된 리소스 요청
    SOCIAL_ID_NOT_FOUND(404,false , "존재하지 않는 SOCIAL ID 입니다."),
    POST_ID_NOT_FOUND(404, false, "존재하지 않는 게시글 ID 입니다."),
    REVIEW_ID_NOT_FOUND(404, false, "존재하지 않는 리뷰 ID 입니다."),
    POST_COMMENT_ID_NOT_FOUND(404, false, "존재하지 않는 게시글 댓글 ID 입니다."),
    PERFUMER_APPLICATION_ID_NOT_FOUND(404, false, "존재하지 않는 조향사 신청 ID 입니다."),
    BOARD_TYPE_ID_NOT_FOUND(404, false, "존재하지 않는 게시판 ID 입니다."),
    INAPPROPRIATE_PERIOD_NUM(404, false, "올바르지 않은 기간 설정 코드입니다."),

    TOKEN_NOT_FOUND(404,false , "토큰이 존재 하지 않습니다."),

    // 409 중복된 리소스 요청
    ALREADY_SAVED_SOCIAL_ID(409,false , "이미 존재하는 SOCIAL_ID 입니다."),
    ALREADY_APPROVED_PERFUMER_APPLICATION(409, false, "이미 승인이 완료된 신청입니다."),

    // 500 서버 에러
    INTERNAL_SERVER_ERROR(500,false , "서버 에러 입니다.");

    private final int code;
    private boolean success;
    private final String msg;

}
