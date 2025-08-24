package com.webstore.order_service.order;

import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void handleOrderCreated(OrderConfirmation orderConfirmation);
    OrderResponse findOrderById(Long orderId);
    Page<OrderResponse> findOrderByUserId(Long userId, Pageable pageable);
    void updateOrderStatus(OrderRequest orderRequest);
    Long isOrderOwner(Long orderId);
}
