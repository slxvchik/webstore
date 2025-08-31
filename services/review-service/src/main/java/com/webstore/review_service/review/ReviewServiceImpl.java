package com.webstore.review_service.review;

import com.webstore.review_service.exceptions.ReviewException;
import com.webstore.review_service.exceptions.ReviewNotFoundException;
import com.webstore.review_service.media.MediaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final MediaClient mediaClient;
    private final ReviewMapper reviewMapper;

    @Override
    public String createReview(ReviewRequest reviewRequest) {

        if (reviewRepo.existsByProductIdAndUserId(reviewRequest.productId(), reviewRequest.userId())) {
            throw new ReviewException("Review already exists");
        }

        Review review = Review.builder()
                .userId(reviewRequest.userId())
                .productId(reviewRequest.productId())
                .text(reviewRequest.text())
                .rating(reviewRequest.rating())
                .build();

        List<String> images = new ArrayList<>();

        if (reviewRequest.attachmentImages() != null) {
            images = mediaClient.uploadImages(reviewRequest.attachmentImages());
            review.setAttachedImages(images);
        }

        Review savedReview;

        try {
            savedReview = reviewRepo.save(review);
        } catch (Exception e) {
            if (!images.isEmpty()) {
                mediaClient.deleteImages(images);
            }
            throw new ReviewException("Error creating review: " + e.getMessage(), e.getCause());
        }

        return savedReview.getId();
    }

    @Override
    public ReviewResponse getReviewResponseById(String reviewId) {
        return reviewRepo.findById(reviewId)
                .map(reviewMapper::toReviewResponse)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }

    @Override
    public ReviewResponse updateReview(String reviewId, ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public void deleteReview(String reviewId) {

    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId, Pageable pageable) {
        return List.of();
    }

    private Review findReviewByProductAndUser(String productId, String userId) {
        return reviewRepo.findByProductIdAndUserId(productId, userId).orElseThrow(
                () -> new ReviewNotFoundException("Review not found for productId: " + productId + ", userId: " + userId)
        );
    }

}
