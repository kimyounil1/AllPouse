package com.perfume.allpouse.exception;

import com.perfume.allpouse.model.reponse.CommonResponse;
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

    @ExceptionHandler({CustomeException.class})
    protected CommonResponse handleCustomException(CustomeException ex) {
        return responseService.getErrorResponse(ex.getExceptionEnum().getCode(), ex.getExceptionEnum().getMsg());
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    protected CommonResponse handleMissingParamException(Exception ex) {
        return responseService.getErrorResponse(INVALID_PARAMETER.getCode(), INVALID_PARAMETER.getMsg());
    }

    @ExceptionHandler({ Exception.class })
    protected CommonResponse handleServerException(Exception ex) {
        return responseService.getErrorResponse(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }



}
