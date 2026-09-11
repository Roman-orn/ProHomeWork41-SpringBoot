package org.example.springboot.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends HttpBaseException {
    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.NOT_FOUND, message);
    }
}
