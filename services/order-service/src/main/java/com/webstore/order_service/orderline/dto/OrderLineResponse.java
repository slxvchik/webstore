package com.webstore.order_service.orderline.dto;

import java.math.BigDecimal;

public record OrderLineResponse(
        Long id,
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal pricePerUnit
        // todo: ProductShortResponse product (replace productId)
) {
}
