package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.ProductPurchaseRequest;
import com.webstore.product_service.product.dto.ProductPurchaseResponse;
import com.webstore.product_service.product.dto.ProductRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;

import java.util.List;

public interface ProductService {
    boolean productExistsById(Long id);
    List<ProductShortResponse> findAllProducts();
    Long createProduct(ProductRequest request);
    void updateProduct(ProductRequest request);
    void deleteProduct(Long id);
    ProductShortResponse findProductById(Long id);
    ProductShortResponse findProductByName(String name);
    ProductPurchaseResponse purchaseProducts(ProductPurchaseRequest requests);
}
