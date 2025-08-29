package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryMapper;
import com.webstore.product_service.category.dto.CategoryResponse;
import com.webstore.product_service.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public Product toProduct(ProductRequest productRequest, Category category) {
        return Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .quantity(productRequest.quantity())
                .category(category)
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                "",
                new ArrayList<>(),
                product.getCreated(),
                product.getRating(),
                product.getReviewsCount(),
                categoryMapper.toCategoryResponse(product.getCategory())
        );
    }

    public ProductShortResponse toShortProductResponse(Product product) {
        return new ProductShortResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                "",
                product.getRating(),
                product.getReviewsCount()
        );
    }

    public ProductPurchaseItemRequest toProductPurchaseItemRequest(Product product, Integer quantity) {
        return new ProductPurchaseItemRequest(
                product.getId(),
                quantity
        );
    }

    public ProductPurchaseItemResponse toProductPurchaseItemResponse(Product product, Integer quantity) {
        return new ProductPurchaseItemResponse(
                product.getId(),
                product.getPrice(),
                quantity
        );
    }

}
