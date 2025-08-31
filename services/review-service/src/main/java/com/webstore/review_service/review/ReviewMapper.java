package com.webstore.review_service.review;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUserId(),
                review.getText(),
                review.getRating(),
                review.getAttachedImages()
        );
    }
}
