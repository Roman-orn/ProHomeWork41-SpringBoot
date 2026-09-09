package org.example.springboot.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpBaseException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpBaseException(HttpBaseException ex) {
        return ResponseEntity.status(ex.httpStatus)
                .body(new ErrorResponseDto(ex.errorCode, ex.getMessage()));
    }
}
