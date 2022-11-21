package com.perfume.allpouse.model.reponse;

import com.perfume.allpouse.exception.ExceptionEnum;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final boolean success;
    private final String error;
    private final int code;
    private final String msg;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ExceptionEnum exceptionEnum) {
        return ResponseEntity
                .status(exceptionEnum.getCode())
                .body(ErrorResponse.builder()
                        .success(exceptionEnum.isSuccess())
                        .error(exceptionEnum.name())
                        .code(exceptionEnum.getCode())
                        .msg(exceptionEnum.getMsg())
                        .build()
                );
    }
}