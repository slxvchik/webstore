package com.webstore.order_service.product.dto;

import java.util.List;

public record ProductPurchaseRequest(
        List<ProductPurchaseItemRequest> products
) {
}
