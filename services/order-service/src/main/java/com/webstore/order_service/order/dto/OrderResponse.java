package com.webstore.order_service.order.dto;

import com.webstore.order_service.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus status,
        BigDecimal total,
        LocalDateTime created
) {
}
