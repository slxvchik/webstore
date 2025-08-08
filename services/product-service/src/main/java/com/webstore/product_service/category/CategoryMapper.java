package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .id(request.id())
                .name(request.name())
                .slug(request.slug())
                .description(request.description())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription()
        );
    }
}
