package com.lightcc.motd.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 정의된 에러 핸들링
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationExceptionHandler(ApplicationException e) {
        log.error("########### Application error occurs: {}", e.toString());
        return ErrorResponseEntity.toResponseEntity(e.getData().getMessage());
    }

    /**
     * 미정의된 에러 핸들링
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> globalExceptionHandler(RuntimeException e) {
        log.error("########### Unexpected error occurs: {}", e.toString());
        return ErrorResponseEntity.toResponseEntity(null);
    }
}