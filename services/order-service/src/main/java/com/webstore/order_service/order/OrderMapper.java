package com.webstore.order_service.order;

import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.dto.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(OrderConfirmation orderConfirmation) {
        return Order.builder()
                .userId(orderConfirmation.userId())
                .status(orderConfirmation.status())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreated()
        );
    }

}