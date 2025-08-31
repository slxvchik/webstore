package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import com.webstore.product_service.exception.CategoryValidateException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> allCategories = categoryRepo.findAll();
        return buildCategoryTree(allCategories);
    }

    @Override
    public Long createCategory(CategoryRequest request) {

        validateCategoryRequestUniques(request);

        Category category = categoryMapper.toCategory(request);

        if (request.parentId() != null) {
            Category parentCategory = findCategoryById(request.parentId());
            category.setParentCategory(parentCategory);
            category.setDepth(parentCategory.getDepth() + 1);
        } else {
            category.setDepth(0);
        }

        category.setPath(generatePath(category));

        return categoryRepo.save(category).getId();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {

        var category = findCategoryById(categoryId);

        validateCategoryRequestUniques(request, categoryId);

        String oldPath = category.getPath();

        category.setName(request.name());
        category.setDescription(request.description());
        category.setSlug(request.slug());

        if (request.parentId() != null && (
                category.getParentCategory() == null ||
                !category.getParentCategory().getId().equals(request.parentId())
            )
        ) {

            var parentCategory = findCategoryById(request.parentId());

            category.setParentCategory(parentCategory);
            category.setDepth(parentCategory.getDepth() + 1);
        } else if (category.getParentCategory() != null) {
            category.setParentCategory(null);
            category.setDepth(0);
        }

        String newPath = generatePath(category);
        category.setPath(newPath);

        Category updatedCategory = categoryRepo.save(category);

        if (!oldPath.equals(newPath)) {
            updateDescendantsPaths(oldPath, newPath);
        }

        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepo.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        return categoryMapper.toCategoryResponse(findCategoryById(categoryId));
    }

    @Override
    public List<Category> getCategoryDescendants(String path) {
        return categoryRepo.findCategoriesByPathStartingWith(convertUrlToCategoryPath(path));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + categoryId + "not found"));
    }

    private List<CategoryResponse> buildCategoryTree(List<Category> categories) {
        Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();

        for (Category category : categories) {
            categoryResponseMap.put(category.getId(), categoryMapper.toCategoryResponse(category));
        }

        List<CategoryResponse> rootCategoryResponses = new ArrayList<>();

        for (Category category : categories) {

            CategoryResponse currentCategoryResponse = categoryResponseMap.get(category.getId());

            if (currentCategoryResponse.getParentId() != null) {

                Long parentId = currentCategoryResponse.getParentId();
                CategoryResponse parentCategoryResponse = categoryResponseMap.get(parentId);

                if (parentCategoryResponse != null) {

                    // set children category to parent
                    parentCategoryResponse.getChildren().add(currentCategoryResponse);

                } else {

                    rootCategoryResponses.add(currentCategoryResponse);

                }
            } else {

                rootCategoryResponses.add(currentCategoryResponse);

            }
        }

        return rootCategoryResponses;
    }

    private void updateDescendantsPaths(String oldPath, String newPath) {
        List<Category> descendants = categoryRepo.findCategoriesByPathStartingWith(oldPath + "/");
        for (Category descendant : descendants) {
            String newDescendantPath = descendant.getPath().replaceFirst("^" + oldPath, newPath);
            descendant.setPath(newDescendantPath);
            categoryRepo.save(descendant);
        }
    }

    private String generatePath(Category category) {
        if (category.getParentCategory() != null) {
            return category.getParentCategory().getPath() + "/" + category.getSlug();
        } else {
            return "/" + category.getSlug();
        }
    }

    private String convertUrlToCategoryPath(String urlPath) {
        String cleanPath = urlPath.replaceAll("^/|/$", "");

        if (cleanPath.isEmpty()) {
            return "/";
        }

        return "/" + cleanPath;
    }

    private void validateCategoryRequestUniques(CategoryRequest category) {
        validateCategoryRequestUniques(category, null);
    }

    private void validateCategoryRequestUniques(CategoryRequest category, Long excludeId) {
        List<String> errors = new ArrayList<>();
        if (excludeId == null) {
            if (categoryRepo.existsByName(category.name())) {
                errors.add("Category with name " + category.name() + " already exists");
            }
            if (categoryRepo.existsBySlug(category.slug())) {
                errors.add("Category with slug " + category.slug() + " already exists");
            }
        } else {
            if (categoryRepo.existsByNameAndIdNot(category.name(), excludeId)) {
                errors.add("Category with name " + category.name() + " already exists");
            }
            if (categoryRepo.existsBySlugAndIdNot(category.slug(), excludeId)) {
                errors.add("Category with slug " + category.slug() + " already exists");
            }
        }
        if (!errors.isEmpty()) {
            throw new CategoryValidateException(String.join(" ", errors));
        }
    }
}
