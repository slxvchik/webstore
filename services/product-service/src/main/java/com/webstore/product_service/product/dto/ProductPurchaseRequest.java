package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductPurchaseRequest (
        @Positive(message = "Total price must be positive")
        BigDecimal totalPrice,
        @NotEmpty(message = "Products must be not empty")
        List<ProductPurchaseItem> products
) {
}
