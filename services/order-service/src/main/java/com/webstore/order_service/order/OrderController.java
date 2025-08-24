package com.webstore.order_service.order;

import com.webstore.order_service.order.dto.OrderRequest;
import com.webstore.order_service.order.dto.OrderResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<OrderResponse>> findAllByAuthUserId(
            @RequestParam(value = "page") @Min(1) Integer page,
            @AuthenticationPrincipal Long userId
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(orderService.findOrderByUserId(userId, pageable));
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
    public ResponseEntity<Page<OrderResponse>> findAllByUser(
            @RequestParam(value = "page") @Min(1) Integer page,
            @PathVariable("user-id") Long userId
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(orderService.findOrderByUserId(userId, pageable));
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
