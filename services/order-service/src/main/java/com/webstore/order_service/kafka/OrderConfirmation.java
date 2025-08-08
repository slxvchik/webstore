package com.webstore.order_service.kafka;

import com.webstore.order_service.order.OrderStatus;
import com.webstore.order_service.product.dto.ProductPurchaseItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        Long userId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<ProductPurchaseItem> products
) {
}
