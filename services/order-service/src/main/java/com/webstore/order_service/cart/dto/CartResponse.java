package com.webstore.order_service.cart.dto;

import java.math.BigDecimal;

public record CartResponse(
        Long id,
        Long productId,
        BigDecimal productPrice,
        Long userId,
        Integer quantity
) {
}
