package com.perfume.allpouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{

    private final ExceptionEnum exceptionEnum;

}
