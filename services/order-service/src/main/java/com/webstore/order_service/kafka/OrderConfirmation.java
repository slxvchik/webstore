package com.webstore.order_service.kafka;

import com.webstore.order_service.order.OrderStatus;
import com.webstore.order_service.product.dto.ProductPurchaseItemRequest;

import java.util.List;

public record OrderConfirmation(
        String userId,
        OrderStatus status,
        List<ProductPurchaseItemRequest> products
) {
}
