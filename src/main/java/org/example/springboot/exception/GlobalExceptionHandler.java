package org.example.springboot.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpBaseException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpBaseException(HttpBaseException ex) {
        return ResponseEntity.status(ex.httpStatus)
                .body(new ErrorResponseDto(
                        ex.errorCode,
                        ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(
                        ErrorCode.INPUT_PARAMETERS_VALIDATION_FAILED,
                        message));
    }
}
