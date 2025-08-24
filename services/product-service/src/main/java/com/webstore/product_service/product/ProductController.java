package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<Page<ProductShortResponse>> findProducts(
            ProductSearchRequest productSearchRequest
    ) {

        if (productSearchRequest.minPrice() != null && productSearchRequest.maxPrice() != null &&
                productSearchRequest.minPrice().compareTo(productSearchRequest.maxPrice()) > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "minPrice cannot be greater than maxPrice");
        }

        ProductSearchCriteria productSearchCriteria = ProductSearchCriteria.builder()
                .ids(productSearchRequest.ids())
                .name(productSearchRequest.name())
                .categoryIds(productSearchRequest.categoryIds())
                .minPrice(productSearchRequest.minPrice())
                .maxPrice(productSearchRequest.maxPrice())
                .build();

        Pageable productPages = PageRequest.of(productSearchRequest.page(), productSearchRequest.size(), parseSort(productSearchRequest.sort()));

        return ResponseEntity.ok(productService.searchProducts(productSearchCriteria, productPages));
    }

    private Sort parseSort(String sort) {

        String[] sortParams = sort.split(",");

        if (sortParams.length != 2) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "Invalid sort parameter. Use format: field,direction");
        }

        String field = sortParams[0];
        Sort.Direction direction;

        try {
            direction = Sort.Direction.fromString(sortParams[1]);
        } catch (IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "Invalid sort direction. Use 'asc' or 'desc'");
        }

        List<String> allowedSortFields = Arrays.asList("name", "price", "created");
        if (!allowedSortFields.contains(field)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "Invalid sort field. Allowed fields: " + allowedSortFields);
        }

        return Sort.by(direction, field);
    }

    @GetMapping("/{product-id}/short")
    public ResponseEntity<ProductShortResponse> showShortProduct(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductShortResponse> showProduct(
            @PathVariable("product-id") Long productId
    ) {

        return ResponseEntity.ok(productService.findProductById(productId));
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
     */

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @PostMapping
    public ResponseEntity<Long> createProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        productService.updateProduct(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("product-id") Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }

    /***
     * ==========================MICROSERVICE RIGHTS==========================
     */

    @PreAuthorize("hasAnyAuthority('MICROSERVICE')")
    @PostMapping("/purchase")
    public ResponseEntity<ProductPurchaseResponse> purchaseProduct(
            @RequestBody @Valid ProductPurchaseRequest request
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @PreAuthorize("hasAnyAuthority('MICROSERVICE')")
    @GetMapping("/{product-id}/exists")
    public ResponseEntity<Boolean> productExists(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.productExistsById(productId));
    }

}
