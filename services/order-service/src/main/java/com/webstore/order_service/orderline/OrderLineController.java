package com.webstore.order_service.orderline;

import com.webstore.order_service.orderline.dto.OrderLineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-lines")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;

//    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
//    @GetMapping("/order/{order-id}")
//    public ResponseEntity<List<OrderLineResponse>> findByOrderId(
//            @PathVariable("order-id") Long orderId
//    ) {
//        return ResponseEntity.ok(orderLineService.findAllByOrderId(orderId));
//    }
}
