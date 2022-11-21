package com.perfume.allpouse.exception;

import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ErrorResponse;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.perfume.allpouse.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;
import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;

@RestControllerAdvice
public class GlobalExceptionHandler {

    ResponseServiceImpl responseService;

    @ExceptionHandler({CustomException.class})
    private ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ErrorResponse.toResponseEntity(ex.getExceptionEnum());
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    protected ResponseEntity<ErrorResponse> handleMissingParamException(Exception ex) {
        return ErrorResponse.toResponseEntity(INVALID_PARAMETER);
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ErrorResponse> handleServerException(Exception ex) {
        return ErrorResponse.toResponseEntity(INTERNAL_SERVER_ERROR);
    }



}
