package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
                .active(request.active())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .path(category.getPath())
                .depth(category.getDepth())
                .parentId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .active(category.getActive())
                .children(new ArrayList<>())
                .build();
    }

}
