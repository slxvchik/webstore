package com.webstore.order_service.product.dto;

public record ProductPurchaseItemRequest(
        Long productId,
        Integer quantity
) {
}
