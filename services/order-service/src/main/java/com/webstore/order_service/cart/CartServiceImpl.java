package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import com.webstore.order_service.exception.BusinessException;
import com.webstore.order_service.kafka.OrderProducer;
import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.OrderStatus;
import com.webstore.order_service.product.ProductClient;
import com.webstore.order_service.product.dto.ProductPurchaseItemRequest;
import com.webstore.order_service.product.dto.ProductPurchaseItemResponse;
//import com.webstore.order_service.user.UserClient;
import com.webstore.order_service.product.dto.ProductShortResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final ProductClient productClient;

    private final OrderProducer orderProducer;

    @Override
    public Page<CartResponse> findAllCarts(Pageable pageRequest) {

        Page<Cart> cartsPage = cartRepository.findAll(pageRequest);

        List<ProductShortResponse> products = productClient.findProductsByIds(cartsPage.stream().map(Cart::getProductId).toList());

        return cartMapper.toPageableListCartResponse(cartsPage, products);
    }

    @Override
    public Page<CartResponse> findUserCartByUserId(Long userId, Pageable pageRequest) {

        Page<Cart> cartsPage = cartRepository.findAllByUserId(userId, pageRequest);

        List<ProductShortResponse> products = productClient.findProductsByIds(cartsPage.stream().map(Cart::getProductId).toList());

        return cartMapper.toPageableListCartResponse(cartsPage, products);
    }

    @Override
    public Long createCart(Long userId, CartRequest request) {

        Cart cart = cartMapper.toCart(userId, request);

        var newCart = cartRepository.save(cart);

        return newCart.getId();
    }

    @Override
    public void updateCart(CartRequest request) {

        if (!productClient.productExists(request.productId())) {
            throw new EntityNotFoundException("Product with ID: " + request.productId() + " not found");
        }

        var cart = cartRepository.findById(request.id())
                .orElseThrow(
                () -> new EntityNotFoundException("Cart with ID: " + request.id() + " not found")
        );

        if (!cart.getProductId().equals(request.productId())) {
            throw new BusinessException("It is not possible to change the product or user ID");
        }

        if (isValidQuantity(request.quantity())) {
            cartRepository.save(cart);
        } else {
            cartRepository.delete(cart);
        }

    }

    @Override
    public void deleteCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new EntityNotFoundException("Cart with ID: " + cartId + " not found");
        }
        cartRepository.deleteById(cartId);
    }


    @Override
    @Transactional
    public void purchaseProducts(Long userId) {

        var userCart = cartRepository.findAllByUserId(userId);

        var productsPurchase = userCart.stream()
                .map(cart -> new ProductPurchaseItemRequest(
                        cart.getProductId(),
                        cart.getQuantity()
                )).toList();

        // Kafka: topic: create-order
        orderProducer.sendOrderConfirmation(
            new OrderConfirmation(
                userId,
                OrderStatus.CREATED,
                productsPurchase
            )
        );

    }

    @Override
//    @KafkaListener(
//            topics = "order-confirmed",
//            groupId = "orderGroup"
//    )
    public void deleteUserCart(Long userId) {
//        cartRepository.deleteAllByUserId(userId);
    }

    @Override
    public boolean isUserCart(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with ID: " + cartId + " not found"));
        return cart.getUserId().equals(userId);
    }

    private boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}
