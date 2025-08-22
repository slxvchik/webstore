package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.ProductPurchaseRequest;
import com.webstore.product_service.product.dto.ProductPurchaseResponse;
import com.webstore.product_service.product.dto.ProductRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductShortResponse>> findProducts() {
        return null;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/{product-id}/short") ///"{product-id}/short"
    public ResponseEntity<ProductShortResponse> showShortProduct(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/{product-id}")
    public ResponseEntity<ProductShortResponse> showProduct(
            @PathVariable("product-id") Long productId
    ) {

        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/{product-id}/exists")
    public ResponseEntity<Boolean> productExists(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.productExistsById(productId));
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
     */

    @GetMapping
    public ResponseEntity<List<ProductShortResponse>> findAll() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

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

    // Deleting products from db
    // Close this api-gateway (for order-service only)
    @PostMapping("/purchase")
    public ResponseEntity<ProductPurchaseResponse> purchaseProduct(
            @RequestBody @Valid ProductPurchaseRequest request
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

}
