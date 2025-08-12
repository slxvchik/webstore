package com.webstore.order_service.order;

import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import com.webstore.order_service.orderline.OrderLine;
import com.webstore.order_service.orderline.OrderLineRepository;
import com.webstore.order_service.product.ProductClient;
import com.webstore.order_service.product.dto.ProductPurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderMapper orderMapper;

    @Override
    @KafkaListener(
            topics = "create-order",
            groupId = "orderGroup"
    )
    public void handleOrderCreated(OrderConfirmation orderConfirmation) {

        // Product service - purchaseProducts (Reduce the quantity)
        var purchasedProducts = productClient.purchaseProducts(
                new ProductPurchaseRequest(
                    orderConfirmation.totalPrice(),
                    orderConfirmation.products()
                )
        );

        // order and order lines creation
        var order = orderRepository.save(Order.builder()
                .userId(orderConfirmation.userId())
                .status(OrderStatus.CREATED)
                .total(purchasedProducts.totalPrice())
                .build());

        var orderLines = purchasedProducts.products().stream().map(product ->
            OrderLine.builder()
                    .order(order)
                    .productId(product.productId())
                    .quantity(product.quantity())
                    .pricePerItem(product.price())
                    .build()
        ).toList();

        orderLineRepository.saveAll(orderLines);

        // Здесь логика:
        // 3. Отправить событие "order-confirmed" в kafka
    }

    @Override
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + orderId + " not found"));
    }

    @Override
    public List<OrderResponse> findOrderByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public void updateOrderStatus(OrderRequest orderRequest) {
        var order = orderRepository.findById(orderRequest.id())
                .orElseThrow(
                        () -> new EntityNotFoundException("Order with id: " + orderRequest.id() + " not found")
                );
        order.setStatus(orderRequest.status());
        orderRepository.save(order);
    }

    @Override
    public Long isOrderOwner(Long orderId) {
        return orderRepository.findById(orderId)
                .map(Order::getUserId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order with id: " + orderId + " not found")
                );
    }
}
