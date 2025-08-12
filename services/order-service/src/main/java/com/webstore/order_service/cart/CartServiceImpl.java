package com.webstore.order_service.cart;

import com.webstore.order_service.cart.dto.CartRequest;
import com.webstore.order_service.cart.dto.CartResponse;
import com.webstore.order_service.exception.BusinessException;
import com.webstore.order_service.kafka.OrderProducer;
import com.webstore.order_service.kafka.OrderConfirmation;
import com.webstore.order_service.order.OrderStatus;
import com.webstore.order_service.product.ProductClient;
import com.webstore.order_service.product.dto.ProductPurchaseItem;
//import com.webstore.order_service.user.UserClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    private final ProductClient productClient;
//    private final UserClient userClient;

    private final OrderProducer orderProducer;

    @Override
    public List<CartResponse> findAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(cartMapper::toCartResponse)
                .toList();
    }

    @Override
    public CartResponse findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .map(cartMapper::toCartResponse)
                .orElseThrow(
                () -> new EntityNotFoundException("Cart with ID: " + cartId + " not found")
            );
    }

    @Override
    public List<CartResponse> findUserCartByUserId(Long userId) {
        return cartRepository.findAllByUserIdOrderByCreated(userId)
                .stream()
                .map(cartMapper::toCartResponse)
                .toList();
    }

    @Override
    public Long createCart(CartRequest request) {

        // var user = userClient.findUserById(request.userId())
        //         .orElseThrow(() -> new EntityNotFoundException("User with ID: " + request.userId() + " not found"));

//        if (!userClient.userExists(request.userId())) {
//            throw new EntityNotFoundException("User with ID: " + request.userId() + " not found");
//        }

        var product = productClient.findProductById(request.productId());

        Cart cart = cartMapper.toCart(request);

        cart.setProductPrice(product.price());

        var newCart = cartRepository.save(cart);

        return newCart.getId();
    }

    @Override
    public void updateCart(CartRequest request) {

//        if (userClient.userExists(request.userId())) {
//            throw new EntityNotFoundException("User with ID: " + request.userId() + " not found");
//        }

        if (!productClient.productExists(request.productId())) {
            throw new EntityNotFoundException("Product with ID: " + request.productId() + " not found");
        }

        var cart = cartRepository.findById(request.id())
                .orElseThrow(
                () -> new EntityNotFoundException("Cart with ID: " + request.id() + " not found")
        );

        if (!cart.getProductId().equals(request.productId()) || !cart.getUserId().equals(request.userId())) {
            throw new BusinessException("It is not possible to change the product and user ID");
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

        var userCart = cartRepository.findAllByUserIdOrderByCreated(userId);

        // collecting ProductPurchaseRequest - total amount, product id, product quantity
        var productsPurchase = userCart.stream()
                .map(cart -> new ProductPurchaseItem(
                        cart.getProductId(),
                        cart.getProductPrice(),
                        cart.getQuantity()
                )).toList();

        var cartTotalAmount = userCart.stream()
            .map(cartItem ->
                cartItem.getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            )
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Kafka: topic: create-order
        orderProducer.sendOrderConfirmation(
            new OrderConfirmation(
                userId,
                OrderStatus.CREATED,
                cartTotalAmount,
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

    private boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}
