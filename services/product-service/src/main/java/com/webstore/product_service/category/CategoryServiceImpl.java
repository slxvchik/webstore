package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import com.webstore.product_service.exception.CategoryValidateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepo.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long createProduct(CategoryRequest request) {
        var newCategory = categoryMapper.toCategory(request);
        validateCategoryUniques(newCategory);
        return categoryRepo.save(newCategory).getId();
    }

    @Override
    public void updateCategory(CategoryRequest request) {
        var category = categoryRepo.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Category " + request.id() + "not found"));
        validateCategoryUniques(category);
        categoryMerge(category, request);
        categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new EntityNotFoundException("Category with id: " + categoryId + "not found");
        }
        categoryRepo.deleteById(categoryId);
    }

    @Override
    public CategoryResponse findCategoryById(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("Category with id: " + categoryId + "not found")
                );
    }

    @Override
    public CategoryResponse findCategoryBySlug(String slug) {
        return categoryRepo.findBySlug(slug)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("Category with slug: " + slug + "not found")
                );
    }

    private void categoryMerge(Category category, CategoryRequest request) {
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setDescription(request.description());
    }

    private void validateCategoryUniques(Category category) {
        List<String> errors = new ArrayList<>();
        if (categoryRepo.existsByName(category.getName())) {
            errors.add("Category with name " + category.getName() + " already exists");
        }
        if (categoryRepo.existsBySlug(category.getSlug())) {
            errors.add("Category with slug " + category.getSlug() + " already exists");
        }
        if (!errors.isEmpty()) {
            throw new CategoryValidateException(String.join(" ", errors));
        }
    }
}
