package com.webstore.product_service.product.dto;

import com.webstore.product_service.category.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse
(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        String thumbnail,
        List<String> images,
        LocalDateTime created,
        Float rating,
        Integer reviewsCount,
        Category category
){
}
