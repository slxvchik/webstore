package com.webstore.product_service.category.dto;

public record CategoryShortResponse(
        Long id,
        String name,
        String slug,
        String path
) {
}
