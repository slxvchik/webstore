package com.webstore.order_service.cart.dto;

import jakarta.validation.constraints.NotNull;

public record CartRequest(
        Long id,
        @NotNull(message = "Product id can't be null")
        Long productId,
        @NotNull(message = "User id can't be null")
        Long userId,
        @NotNull(message = "Quantity id can't be null")
        Integer quantity
) {
}
