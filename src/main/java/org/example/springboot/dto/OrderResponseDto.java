package org.example.springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        LocalDateTime creationDate,
        List<OrderItemResponseDto> items,
        BigDecimal totalCost
) {
}
