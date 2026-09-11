package org.example.springboot.exception;

public record ErrorResponseDto(
        ErrorCode errorCode,
        String message
) {
}
