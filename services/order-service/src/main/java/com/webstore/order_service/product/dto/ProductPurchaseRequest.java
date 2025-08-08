package com.webstore.order_service.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductPurchaseRequest(
        BigDecimal totalPrice,
        List<ProductPurchaseItem> products
) {
}
