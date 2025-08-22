package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public Cart toCart(Long userId, CartRequest request) {
        return Cart.builder()
                .id(request.id())
                .userId(userId)
                .productId(request.productId())
                .quantity(request.quantity())
                .build();
    }

    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getProductId(),
                cart.getProductPrice(),
                cart.getUserId(),
                cart.getQuantity()
        );
    }
}
