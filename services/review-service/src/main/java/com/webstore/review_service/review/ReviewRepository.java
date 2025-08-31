package com.webstore.review_service.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Review> findByProductIdAndUserId(String productId, String userId);

    boolean existsByProductIdAndUserId(String productId, String userId);
}
