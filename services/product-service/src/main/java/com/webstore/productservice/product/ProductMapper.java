package com.webstore.productservice.product;

import com.webstore.productservice.category.Category;
import com.webstore.productservice.product.dto.ProductPurchaseResponse;
import com.webstore.productservice.product.dto.ProductRequest;
import com.webstore.productservice.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
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
                product.getRating(),
                product.getReviewsCount(),
                product.getCategory()
        );
    }

    public ProductPurchaseResponse toproductPurchaseResponse(Product product, Integer quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
