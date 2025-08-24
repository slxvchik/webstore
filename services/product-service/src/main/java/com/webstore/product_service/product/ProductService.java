package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    boolean productExistsById(Long id);
    Long createProduct(ProductRequest request);
    void updateProduct(ProductRequest request);
    void deleteProduct(Long id);
    ProductShortResponse findProductById(Long id);
    ProductPurchaseResponse purchaseProducts(ProductPurchaseRequest requests);
    Page<ProductShortResponse> searchProducts(ProductSearchCriteria productSearchCriteria, Pageable productPages);
}
