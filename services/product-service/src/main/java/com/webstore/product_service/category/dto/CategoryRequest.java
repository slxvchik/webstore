package com.webstore.product_service.category.dto;

import com.webstore.product_service.category.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        Long id,
        String description,
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotBlank(message = "Slug must not be blank")
        String slug,
        Long parentId
) {
}
