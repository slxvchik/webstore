package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
    Long createProduct(CategoryRequest request);
    void updateCategory(CategoryRequest request);
    void deleteCategory(Long id);
    CategoryResponse findCategoryById(Long categoryId);
    CategoryResponse findCategoryBySlug(String slug);
}
