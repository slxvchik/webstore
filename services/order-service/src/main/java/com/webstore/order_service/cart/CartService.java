package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    Page<CartResponse> findAllCarts(Pageable pageRequest);
    Page<CartResponse> findUserCartByUserId(String userId, Pageable pageRequest);
    Long createCart(String userId, CartRequest request);
    void updateCart(CartRequest request);
    void deleteCart(Long cartId);
    void purchaseProducts(String userId);
    void deleteUserCart(String userId);
    boolean isUserCart(String userId, Long cartId);
}
