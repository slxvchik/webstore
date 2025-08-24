package com.webstore.order_service.cart.dto;

import com.webstore.order_service.product.dto.ProductShortResponse;

public record CartResponse(
        Long id,
        ProductShortResponse product,
        Integer quantity
) {
}
