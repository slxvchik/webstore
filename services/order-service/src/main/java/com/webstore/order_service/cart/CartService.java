package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;

import java.util.List;

public interface CartService {
    List<CartResponse> findAllCarts();
    List<CartResponse> findUserCartByUserId(Long userId);
    CartResponse findCartById(Long cartId);
    Long createCart(Long userId, CartRequest request);
    void updateCart(CartRequest request);
    void deleteCart(Long cartId);
    void purchaseProducts(Long userId);
    void deleteUserCart(Long userId);
    boolean isUserCart(Long userId, Long cartId);
}
