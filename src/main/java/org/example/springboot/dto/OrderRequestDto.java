package org.example.springboot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDto(

        @NotEmpty(message = "Products must not be empty")
        List<@Valid ProductOrderRequestDto> products
) {
}
