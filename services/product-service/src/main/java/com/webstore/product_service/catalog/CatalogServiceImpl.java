package com.webstore.product_service.catalog;

import com.webstore.product_service.catalog.dto.CatalogSearchCriteria;
import com.webstore.product_service.product.Product;
import com.webstore.product_service.product.ProductMapper;
import com.webstore.product_service.product.ProductRepository;
import com.webstore.product_service.product.ProductSpecification;
import com.webstore.product_service.product.dto.ProductShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductShortResponse> searchProducts(CatalogSearchCriteria catalogSearchCriteria, Pageable productPages) {
        Specification<Product> spec = ProductSpecification.buildSpecification(catalogSearchCriteria);
        Page<Product> products = productRepo.findAll(spec, productPages);
        return products.map(productMapper::toProductShortResponse);
    }

}
