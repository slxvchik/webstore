package com.webstore.productservice.product.dto;

import com.webstore.productservice.category.Category;

import java.math.BigDecimal;

public record ProductResponse (
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Float rating,
        Integer reviewsCount,
        Category category
) {
}
