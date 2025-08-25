package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import com.webstore.product_service.category.dto.CategoryShortResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .slug(request.slug())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getSlug(),
                category.getPath(),
                category.getDepth(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                category.getActive(),
                category.getSubCategories().stream()
                        .map(this::toShortResponse)
                        .collect(Collectors.toList())
        );
    }

    public CategoryShortResponse toShortResponse(Category category) {
        return new CategoryShortResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getPath()
        );
    }
}
