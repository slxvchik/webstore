package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProductPurchaseRequest (
        @NotEmpty(message = "Products must be not empty")
        List<ProductPurchaseItemRequest> products
) {
}
