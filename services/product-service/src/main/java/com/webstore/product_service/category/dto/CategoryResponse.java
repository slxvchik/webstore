package com.webstore.product_service.category.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
