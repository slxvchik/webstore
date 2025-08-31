package com.webstore.order_service.product;

import com.webstore.order_service.exception.BusinessException;
import com.webstore.order_service.product.dto.ProductPurchaseRequest;
import com.webstore.order_service.product.dto.ProductPurchaseResponse;
import com.webstore.order_service.product.dto.ProductShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    String.format("Failed to purchase products. Status: %s, Response: %s",
                            purchasedProducts.getStatusCode(),
                            purchasedProducts.getBody())
            );
        }

        return purchasedProducts.getBody();
    }

    public List<ProductShortResponse> findProductsByIds(List<Long> productIds) {

        ResponseEntity<List<ProductShortResponse>> productsResponse = restTemplate.exchange(
                productUrl + "/short/batch?ids=" +
                        productIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (productsResponse.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while checking product: " + productsResponse.getStatusCode());
        }

        var products = productsResponse.getBody();

        if (products == null || products.isEmpty()) {
            throw new BusinessException("Products not found or response body is null for IDS: " + productIds);
        }

        return products;
    }
}
