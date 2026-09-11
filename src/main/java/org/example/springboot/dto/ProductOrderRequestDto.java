package org.example.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductOrderRequestDto(

        @NotNull(message = "Product id must not be null")
        Long productId,

        @NotNull(message = "Quantity must not be null")
        @Min(value = 1, message = "Quantity must be greater than 0")
        int quantity
) {
}
