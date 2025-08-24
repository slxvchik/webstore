package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import com.webstore.order_service.product.dto.ProductShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Page<CartResponse> toPageableListCartResponse(Page<Cart> cartsPage, List<ProductShortResponse> products) {

        Map<Long, ProductShortResponse> productsMap = products.stream().collect(Collectors.toMap(ProductShortResponse::id, Function.identity()));

        List<CartResponse> cartResponses = cartsPage.stream()
                .map(cartItem -> new CartResponse(
                        cartItem.getId(),
                        productsMap.get(cartItem.getProductId()),
                        cartItem.getQuantity()
                ))
                .toList();

        return new PageImpl<>(
                cartResponses,
                cartsPage.getPageable(),
                cartsPage.getTotalElements()
        );
    }
}
