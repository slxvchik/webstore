package com.webstore.order_service.product.dto;

import java.math.BigDecimal;

public record ProductPurchaseItem(
        Long productId,
        BigDecimal price,
        Integer quantity
) {
}
