package com.webstore.product_service.catalog;

import com.webstore.product_service.catalog.dto.CatalogSearchCriteria;
import com.webstore.product_service.product.dto.ProductShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    Page<ProductShortResponse> searchProducts(CatalogSearchCriteria catalogSearchCriteria, Pageable productPages);
}
