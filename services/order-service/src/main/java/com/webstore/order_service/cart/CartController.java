package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<List<CartResponse>> getMyCarts(
            @AuthenticationPrincipal Authentication authentication
    ) {
        List<CartResponse> carts = cartService.findUserCartByUserId((long) authentication.getPrincipal());
        return ResponseEntity.ok(carts);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PostMapping("/me")
    public ResponseEntity<Long> createMyCart(
            @RequestBody @Valid CartRequest request,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Long userId = (long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(userId, request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyCart(
            @RequestBody @Valid CartRequest request,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Long userId = (long) authentication.getPrincipal();
        if (!cartService.isUserCart(userId, request.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This cart does not belong to user");
        }
        cartService.updateCart(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @DeleteMapping("/me/{cart-id}")
    public ResponseEntity<Void> deleteMyCart(
            @PathVariable("cart-id") Long cartId,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Long userId = (long) authentication.getPrincipal();
        if (!cartService.isUserCart(userId, cartId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This cart does not belong to user");
        }
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PostMapping("/me/purchase")
    public ResponseEntity<Void> purchaseMyCart(
            @AuthenticationPrincipal Authentication authentication
    ) {
        cartService.purchaseProducts((long) authentication.getPrincipal());
        return ResponseEntity.ok().build();
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CartResponse>> findAll() {
        return ResponseEntity.ok(cartService.findAllCarts());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @DeleteMapping("/{cart-id}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable("cart-id") Long cartId
    ) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/{cart-id}")
    public ResponseEntity<CartResponse> findCart(
            @PathVariable("cart-id") Long cartId
    ) {
        return ResponseEntity.ok(cartService.findCartById(cartId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping("/user/{user-id}")
    public ResponseEntity<List<CartResponse>> findUserCarts(
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.ok(cartService.findUserCartByUserId(userId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @PostMapping("/user/{user-id}")
    public ResponseEntity<Long> createCart(
            @RequestBody @Valid CartRequest request,
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(userId, request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @PostMapping("/user/{user-id}/purchase")
    public ResponseEntity<Void> purchaseCart(
            @PathVariable("user-id") Long userId
    ) {
        cartService.purchaseProducts(userId);
        return ResponseEntity.ok().build();
    }

    // From kafka after order created
//    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<Void> deleteUserCart(
            @PathVariable("user-id") Long userId
    ) {
        cartService.deleteUserCart(userId);
        return ResponseEntity.ok().build();
    }
}
