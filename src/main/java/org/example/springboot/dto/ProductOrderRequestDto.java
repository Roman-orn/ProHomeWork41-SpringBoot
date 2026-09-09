package org.example.springboot.dto;

public record ProductOrderRequestDto(
        Long productId,
        int quantity
) {
}
