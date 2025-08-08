package com.webstore.product_service.category;

import com.webstore.product_service.category.dto.CategoryRequest;
import com.webstore.product_service.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> create(
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createProduct(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCategory(
            @RequestBody CategoryRequest request
    ) {
        categoryService.updateCategory(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> findCategory(
            @PathVariable("category-id") Long categoryId
    ) {
        return ResponseEntity.ok(categoryService.findCategoryById(categoryId));
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("category-id") Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/slug/{category-slug}")
    public ResponseEntity<CategoryResponse> findCategorySlug(
            @PathVariable("category-slug") String categorySlug
    ) {
        return ResponseEntity.ok(categoryService.findCategoryBySlug(categorySlug));
    }

}
