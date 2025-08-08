package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryMapper;
import com.webstore.product_service.product.dto.ProductPurchaseItem;
import com.webstore.product_service.product.dto.ProductRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public ProductShortResponse toShortProductResponse(Product product) {
        return new ProductShortResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getRating(),
                product.getReviewsCount()
        );
    }

    public ProductPurchaseItem toProductPurchaseItem(Product product, Integer quantity) {
        return new ProductPurchaseItem(
                product.getId(),
                product.getPrice(),
                quantity
        );
    }

}
