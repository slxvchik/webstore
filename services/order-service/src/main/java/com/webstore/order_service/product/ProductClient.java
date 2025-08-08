package com.webstore.order_service.product;

import com.webstore.order_service.exception.BusinessException;
import com.webstore.order_service.product.dto.ProductPurchaseRequest;
import com.webstore.order_service.product.dto.ProductPurchaseResponse;
import com.webstore.order_service.product.dto.ProductShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;

    public boolean productExists(Long productId) {

        var product = restTemplate.getForEntity(productUrl + "/" + productId + "/exists", Boolean.class);

        if (product.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while checking product: " + product.getStatusCode());
        }

        return Boolean.TRUE.equals(product.getBody());
    }

    public ProductShortResponse findProductById(Long productId) {

        var productResponse = restTemplate.getForEntity(productUrl + "/" + productId, ProductShortResponse.class);

        if (productResponse.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while checking product: " + productResponse.getStatusCode());
        }

        var product = productResponse.getBody();

        if (product == null) {
            throw new BusinessException("Product not found or response body is null for ID: " + productId);
        }

        return product;
    }

    public ProductPurchaseResponse purchaseProducts(ProductPurchaseRequest purchaseProducts) {

        ResponseEntity<ProductPurchaseResponse> purchasedProducts = restTemplate.postForEntity(
                productUrl + "/purchase",
                purchaseProducts,
                ProductPurchaseResponse.class
        );

        if (!purchasedProducts.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(
                    String.format("Failed to purchase products. Status: %s, Response: %s",
                            purchasedProducts.getStatusCode(),
                            purchasedProducts.getBody())
            );
        }

        return purchasedProducts.getBody();
    }
}
