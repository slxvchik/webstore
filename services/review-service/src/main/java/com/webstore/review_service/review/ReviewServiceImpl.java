package com.webstore.review_service.review;

import com.webstore.review_service.exceptions.ReviewException;
import com.webstore.review_service.exceptions.ReviewNotFoundException;
import com.webstore.review_service.media.MediaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Review review = findReviewById(reviewId);
        review.setRating(reviewRequest.rating());
        review.setText(reviewRequest.text());
        if (reviewRequest.attachmentImages() != null) {
            if (review.getAttachedImages() != null) {
                mediaClient.deleteImages(review.getAttachedImages());
            }
            List<String> images = mediaClient.uploadImages(reviewRequest.attachmentImages());
            review.setAttachedImages(images);
        }
        Review savedReview = reviewRepo.save(review);

        return reviewMapper.toReviewResponse(savedReview);
    }

    @Override
    public void deleteReview(String reviewId) {
        Review review = findReviewById(reviewId);
        if (review.getAttachedImages() != null) {
            mediaClient.deleteImages(review.getAttachedImages());
        }
        reviewRepo.delete(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId, Pageable pageable) {
        List<Review> review = reviewRepo.findByUserId(productId, pageable);
        return review.stream().map(reviewMapper::toReviewResponse).toList();
    }

    private Review findReviewByProductAndUser(String productId, String userId) {
        return reviewRepo.findByProductIdAndUserId(productId, userId).orElseThrow(
                () -> new ReviewNotFoundException("Review not found for productId: " + productId + ", userId: " + userId)
        );
    }

    private Review findReviewById(String reviewId) {
        return reviewRepo.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("Review not found for id: " + reviewId)
        );
    }

}
