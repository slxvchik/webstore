package com.webstore.productservice.product;

import com.webstore.productservice.product.dto.ProductPurchaseRequest;
import com.webstore.productservice.product.dto.ProductPurchaseResponse;
import com.webstore.productservice.product.dto.ProductRequest;
import com.webstore.productservice.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> findAllProducts();
    Long createProduct(ProductRequest request);
    void updateProduct(ProductRequest request);
    void deleteProduct(Long id);
    ProductResponse findProductById(Long id);
    ProductResponse findProductByName(String name);
    List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests);
}
