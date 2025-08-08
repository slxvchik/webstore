package com.webstore.product_service.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductPurchaseResponse (
        BigDecimal totalPrice,
        List<ProductPurchaseItem> products
) {
}
