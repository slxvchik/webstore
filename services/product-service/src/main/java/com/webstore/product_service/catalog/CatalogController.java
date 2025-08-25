package com.webstore.product_service.catalog;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryService;
import com.webstore.product_service.product.ProductService;
import com.webstore.product_service.catalog.dto.CatalogSearchCriteria;
import com.webstore.product_service.catalog.dto.CatalogSearchRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("{*categoryPath}")
    public ResponseEntity<Page<ProductShortResponse>> findProducts(
            @PathVariable String categoryPath,
            CatalogSearchRequest catalogSearchRequest
    ) {

        if (catalogSearchRequest.minPrice() != null && catalogSearchRequest.maxPrice() != null &&
                catalogSearchRequest.minPrice().compareTo(catalogSearchRequest.maxPrice()) > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "minPrice cannot be greater than maxPrice");
        }

        List<Long> categoryIds = categoryService.findCategoryDescendants(categoryPath).stream()
                .map(Category::getId)
                .toList();

        CatalogSearchCriteria catalogSearchCriteria = CatalogSearchCriteria.builder()
                .ids(catalogSearchRequest.ids())
                .name(catalogSearchRequest.name())
                .categoryIds(categoryIds)
                .minPrice(catalogSearchRequest.minPrice())
                .maxPrice(catalogSearchRequest.maxPrice())
                .build();

        return ResponseEntity.ok(productService.searchProducts(catalogSearchCriteria, catalogSearchRequest.pageable()));
    }

//    private Sort parseSort(String sort) {
//
//        String[] sortParams = sort.split(",");
//
//        if (sortParams.length != 2) {
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
//                    "Invalid sort parameter. Use format: field,direction");
//        }
//
//        String field = sortParams[0];
//        Sort.Direction direction;
//
//        try {
//            direction = Sort.Direction.fromString(sortParams[1]);
//        } catch (IllegalArgumentException e) {
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
//                    "Invalid sort direction. Use 'asc' or 'desc'");
//        }
//
//        List<String> allowedSortFields = Arrays.asList("name", "price", "created");
//        if (!allowedSortFields.contains(field)) {
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
//                    "Invalid sort field. Allowed fields: " + allowedSortFields);
//        }
//
//        return Sort.by(direction, field);
//    }
}
