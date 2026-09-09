package org.example.springboot.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,
        String productName,
        int quantity,
        BigDecimal price
) {
}
