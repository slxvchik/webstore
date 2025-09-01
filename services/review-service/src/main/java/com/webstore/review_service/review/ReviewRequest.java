package com.webstore.review_service.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewRequest(
        @NotBlank
        String productId,
        @NotBlank
        String userId,
        @NotBlank
        String text,
        @Min(0) @Max(10)
        Integer rating,
        List<MultipartFile> attachmentImages
) {
}
