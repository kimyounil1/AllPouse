package com.perfume.allpouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomeException extends RuntimeException{

    private final ExceptionEnum exceptionEnum;

}
