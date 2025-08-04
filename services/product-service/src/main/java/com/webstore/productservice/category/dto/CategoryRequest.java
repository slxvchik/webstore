package com.webstore.productservice.category.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        Long id,
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotBlank(message = "Slug must not be blank")
        String slug,
        String description
) {
}
