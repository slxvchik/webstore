package com.webstore.review_service.review;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewResponse(
        String id,
        String productId,
        String userId,
        String text,
        Integer rating,
        List<String> attachmentImages
) {
}
