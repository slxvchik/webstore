package com.webstore.productservice.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest (
        @NotNull(message = "Product is mandatory")
        Long id,
        @Positive(message = "Quantity must be positive")
        Integer quantity
) {
}
