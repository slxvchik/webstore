package com.webstore.order_service.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserIdOrderByCreated(Long userId);
    void deleteAllByUserId(Long userId);
}
