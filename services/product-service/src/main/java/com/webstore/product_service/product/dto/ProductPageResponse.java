package com.webstore.product_service.product.dto;

public record ProductPageResponse(
        ProductResponse productResponse,
        String reviewsResponse
) {
}
