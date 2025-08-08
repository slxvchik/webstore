package com.webstore.order_service.order.dto;

import com.webstore.order_service.order.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull
        Long id,
        @NotEmpty
        OrderStatus status
) {
}
