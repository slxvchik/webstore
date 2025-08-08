package com.webstore.order_service.order;

import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("order-id") Long orderId
    ) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<OrderResponse>> findAllByUser(
            @PathVariable("user-id") Long orderId
    ) {
        return ResponseEntity.ok(orderService.findOrderByUserId(orderId));
    }

    @PutMapping
    public ResponseEntity<Void> updateStatus(
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        orderService.updateOrderStatus(orderRequest);
        return ResponseEntity.ok().build();
    }

}
