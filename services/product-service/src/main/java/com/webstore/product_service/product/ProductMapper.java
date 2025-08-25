package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProductMapper {

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
                product.getThumbnail(),
                product.getImages(),
                product.getCreated(),
                product.getRating(),
                product.getReviewsCount(),
                product.getCategory()
        );
    }

    public ProductShortResponse toShortProductResponse(Product product) {
        return new ProductShortResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getThumbnail(),
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
