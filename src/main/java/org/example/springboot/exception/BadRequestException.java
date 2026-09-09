package org.example.springboot.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpBaseException {
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.BAD_REQUEST, message);
    }
}
