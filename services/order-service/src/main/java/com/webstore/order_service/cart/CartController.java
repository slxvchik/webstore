package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponse>> findAll() {
        return ResponseEntity.ok(cartService.findAllCarts());
    }

    @PostMapping
    public ResponseEntity<Long> createCart(
            @RequestBody @Valid CartRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCart(
            @RequestBody @Valid CartRequest request
    ) {
        cartService.updateCart(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cart-id}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable("cart-id") Long cartId
    ) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cart-id}")
    public ResponseEntity<CartResponse> findCart(
            @PathVariable("cart-id") Long cartId
    ) {
        return ResponseEntity.ok(cartService.findCartById(cartId));
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<CartResponse>> findUserCarts(
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.ok(cartService.findUserCartByUserId(userId));
    }

    @PostMapping("/user/{user-id}")
    public ResponseEntity<Void> purchaseCart(
            @PathVariable("user-id") Long userId
    ) {
        cartService.purchaseProducts(userId);
        return ResponseEntity.ok().build();
    }

    // From kafka after order created
//    @DeleteMapping("/user/{user-id}")
//    public ResponseEntity<Void> deleteUserCart(
//            @PathVariable("user-id") Long userId
//    ) {
//        cartService.deleteUserCart(userId);
//        return ResponseEntity.ok().build();
//    }
}
