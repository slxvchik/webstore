package com.webstore.order_service.order;

import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import com.webstore.order_service.orderline.OrderLine;
import com.webstore.order_service.orderline.OrderLineRepository;
import com.webstore.order_service.product.ProductClient;
import com.webstore.order_service.product.dto.ProductPurchaseRequest;
import com.webstore.order_service.product.dto.ProductPurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        ProductPurchaseResponse purchasedProducts = productClient.purchaseProducts(
                new ProductPurchaseRequest(
                    orderConfirmation.products()
                )
        );

        // order and order lines creation
        Order order = orderRepository.save(Order.builder()
                .userId(orderConfirmation.userId())
                .status(OrderStatus.CREATED)
                .total(purchasedProducts.totalPrice())
                .build());

        List<OrderLine> orderLines = purchasedProducts.products().stream().map(product ->
            OrderLine.builder()
                    .order(order)
                    .productId(product.productId())
                    .pricePerItem(product.price())
                    .quantity(product.quantity())
                    .build()
        ).toList();

        orderLineRepository.saveAll(orderLines);

        // todo:
        // 3. Отправить событие "order-confirmed" в kafka для удаления корзины
        // пользователя и отправления уведомления PM's ADMIN's и а также клиенту о покупке
    }

    @Override
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new EntityNotFoundException("Order with id: " + orderId + " not found"));
    }

    @Override
    public Page<OrderResponse> findOrderByUserId(Long userId, Pageable page) {
        var orders = orderRepository.findAllByUserId(userId, page).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
        return new PageImpl<>(
                orders,
                page,
                orders.size()
        );
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
