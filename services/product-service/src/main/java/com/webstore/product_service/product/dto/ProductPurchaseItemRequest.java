package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductPurchaseItemRequest(
        @NotNull(message = "Product is mandatory")
        Long productId,
        @Positive(message = "Quantity must be positive")
        Integer quantity
) {
}
