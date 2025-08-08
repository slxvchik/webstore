package com.webstore.order_service.order;

import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    void handleOrderCreated(OrderConfirmation orderConfirmation);
    OrderResponse findOrderById(Long orderId);
    List<OrderResponse> findOrderByUserId(Long userId);
    void updateOrderStatus(OrderRequest orderRequest);
}
