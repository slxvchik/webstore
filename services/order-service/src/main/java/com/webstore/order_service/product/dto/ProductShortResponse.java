package com.webstore.order_service.product.dto;

import java.math.BigDecimal;

public record ProductShortResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer quantity,
        Float rating,
        Integer reviewsCount
) {
}
