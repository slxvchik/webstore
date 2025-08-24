package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    Page<CartResponse> findAllCarts(Pageable pageRequest);
    Page<CartResponse> findUserCartByUserId(Long userId, Pageable pageRequest);
    Long createCart(Long userId, CartRequest request);
    void updateCart(CartRequest request);
    void deleteCart(Long cartId);
    void purchaseProducts(Long userId);
    void deleteUserCart(Long userId);
    boolean isUserCart(Long userId, Long cartId);
}
