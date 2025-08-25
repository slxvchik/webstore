package com.webstore.product_service.product;

import com.webstore.product_service.catalog.dto.CatalogSearchCriteria;
import com.webstore.product_service.product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    boolean productExistsById(Long id);
    Long createProduct(ProductRequest request);
    ProductResponse updateProduct(ProductRequest request);
    void deleteProduct(Long id);
    ProductShortResponse findProductShortById(Long id);
    ProductResponse findProductById(Long id);
    ProductPurchaseResponse purchaseProducts(ProductPurchaseRequest requests);
    Page<ProductShortResponse> searchProducts(CatalogSearchCriteria catalogSearchCriteria, Pageable productPages);
}
