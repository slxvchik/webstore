package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<CartResponse>> getMyCarts(
            @PageableDefault(page = 0, size = 10, sort = "created", direction = Sort.Direction.DESC)
            Pageable pageable,
            @AuthenticationPrincipal Authentication authentication
    ) {
        Page<CartResponse> carts = cartService.findUserCartByUserId(authentication.getPrincipal().toString(), pageable);
        return ResponseEntity.ok(carts);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PostMapping("/me")
    public ResponseEntity<Long> createMyCart(
            @RequestBody @Valid CartRequest request,
            @AuthenticationPrincipal Authentication authentication
    ) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(userId, request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyCart(
            @RequestBody @Valid CartRequest request,
            @AuthenticationPrincipal Authentication authentication
    ) {
        String userId = authentication.getPrincipal().toString();
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
        String userId = authentication.getPrincipal().toString();
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
        cartService.purchaseProducts(authentication.getPrincipal().toString());
        return ResponseEntity.ok().build();
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<CartResponse>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "created", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(cartService.findAllCarts(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @DeleteMapping("/{cart-id}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable("cart-id") Long cartId
    ) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping("/user/{user-id}")
    public ResponseEntity<Page<CartResponse>> findUserCarts(
            @PageableDefault(page = 0, size = 10, sort = "created", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable("user-id") String userId
    ) {
        return ResponseEntity.ok(cartService.findUserCartByUserId(userId, pageable));
    }

    // From kafka after order created
//    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<Void> deleteUserCart(
            @PathVariable("user-id") String userId
    ) {
        cartService.deleteUserCart(userId);
        return ResponseEntity.ok().build();
    }
}
