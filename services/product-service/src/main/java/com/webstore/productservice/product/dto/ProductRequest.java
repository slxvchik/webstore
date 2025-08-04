package com.webstore.productservice.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        Long id,
        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 500, message = "Name must be between 3 and 500 characters")
        String name,
        String description,
        @Positive(message = "Available quantity should be positive")
        BigDecimal price,
        @Positive(message = "Quantity is mandatory")
        Integer quantity,
        @PositiveOrZero
        Long categoryId
) {
}
