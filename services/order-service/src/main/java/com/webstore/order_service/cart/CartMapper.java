package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public Cart toCart(CartRequest request) {
        return Cart.builder()
                .id(request.id())
                .userId(request.userId())
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
