package org.example.springboot.dto;

import java.util.List;

public record OrderRequestDto(
        List<ProductOrderRequestDto> products
) {
}
