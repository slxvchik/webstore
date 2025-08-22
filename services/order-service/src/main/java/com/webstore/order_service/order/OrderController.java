package com.webstore.order_service.order;

import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/me/all")
    public ResponseEntity<List<OrderResponse>> findAllByAuthUserId(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrderByUserId(userId));
    }

    // include order-lines
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/me/{order-id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("order-id") Long orderId,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        var orderResponse = orderService.findOrderById(orderId);

        if (!userId.equals(orderResponse.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return ResponseEntity.ok(orderResponse);
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
     */

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping("/user/{user-id}/all")
    public ResponseEntity<List<OrderResponse>> findAllByUser(
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.ok(orderService.findOrderByUserId(userId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable("order-id") Long orderId
    ) {
        var orderResponse = orderService.findOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @PutMapping("/{order-id}")
    public ResponseEntity<Void> updateStatus(
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        orderService.updateOrderStatus(orderRequest);
        return ResponseEntity.ok().build();
    }

}
