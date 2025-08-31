package com.webstore.review_service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> createReview(
            @RequestBody ReviewRequest reviewRequest
    ) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
    }


    @GetMapping("/{review-id}")
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable("review-id") String reviewId
    ) {
        return ResponseEntity.ok(reviewService.getReviewResponseById(reviewId));
    }

    @PutMapping("/{review-id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable("review-id") String reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewRequest));
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("review-id") String reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{product-id}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(
            @PathVariable("product-id") String productId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId, pageable));
    }
}
