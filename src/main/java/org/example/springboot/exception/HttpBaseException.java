package org.example.springboot.exception;

import org.springframework.http.HttpStatus;

public class HttpBaseException extends RuntimeException {

    public ErrorCode errorCode;
    public HttpStatus httpStatus;

    public HttpBaseException(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
