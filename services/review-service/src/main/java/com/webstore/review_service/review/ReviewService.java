package com.webstore.review_service.review;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    String createReview(ReviewRequest reviewRequest);

    ReviewResponse getReviewResponseById(String reviewId);

    ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest);

    void deleteReview(String reviewId);

    List<ReviewResponse> getReviewsByProductId(String productId, Pageable pageable);
}
