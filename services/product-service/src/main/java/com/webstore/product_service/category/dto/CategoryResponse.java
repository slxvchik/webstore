package com.webstore.product_service.category.dto;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String slug,
        String path,
        Integer depth,
        Long parentId,
        Boolean active,
        List<CategoryShortResponse> children
) {
}
