package com.webstore.productservice.category.dto;

import jakarta.persistence.Column;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
