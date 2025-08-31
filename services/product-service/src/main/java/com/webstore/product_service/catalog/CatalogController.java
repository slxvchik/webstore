package com.webstore.product_service.catalog;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryService;
import com.webstore.product_service.catalog.dto.CatalogSearchCriteria;
import com.webstore.product_service.catalog.dto.CatalogSearchRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@AllArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;
    private final CategoryService categoryService;

    @GetMapping("{*categoryPath}")
    public ResponseEntity<Page<ProductShortResponse>> findProducts(
            @PathVariable String categoryPath,
            CatalogSearchRequest catalogSearchRequest,
            @PageableDefault(page = 0, size = 10, sort = "created", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        if (catalogSearchRequest.minPrice() != null && catalogSearchRequest.maxPrice() != null &&
                catalogSearchRequest.minPrice().compareTo(catalogSearchRequest.maxPrice()) > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "minPrice cannot be greater than maxPrice");
        }

        List<Long> categoryIds = categoryService.getCategoryDescendants(categoryPath).stream()
                .map(Category::getId)
                .toList();

        CatalogSearchCriteria catalogSearchCriteria = CatalogSearchCriteria.builder()
                .ids(catalogSearchRequest.ids())
                .name(catalogSearchRequest.name())
                .categoryIds(categoryIds)
                .minPrice(catalogSearchRequest.minPrice())
                .maxPrice(catalogSearchRequest.maxPrice())
                .build();

        return ResponseEntity.ok(catalogService.searchProducts(catalogSearchCriteria, pageable));
    }
}
