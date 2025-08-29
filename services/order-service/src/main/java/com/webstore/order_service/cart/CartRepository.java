package com.webstore.order_service.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByUserId(String userId, Pageable pageRequest);
    void deleteAllByUserId(String userId);
    List<Cart> findAllByUserId(String userId);
}
