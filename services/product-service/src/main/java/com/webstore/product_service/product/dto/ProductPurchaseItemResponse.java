package com.webstore.product_service.product.dto;

import java.math.BigDecimal;

public record ProductPurchaseItemResponse(
        Long productId,
        BigDecimal price,
        Integer quantity
) {
}
