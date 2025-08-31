package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        Long id,
        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 500, message = "Name must be between 3 and 500 characters")
        String name,
        String description,
        Long categoryId,
        @Positive(message = "Available quantity should be positive")
        BigDecimal price,
        @Positive(message = "Quantity is mandatory")
        Integer quantity,
        MultipartFile thumbnail,
        @Max(20)
        List<MultipartFile> gallery
) {
}
