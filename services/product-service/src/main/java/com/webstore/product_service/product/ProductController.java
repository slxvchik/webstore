package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping("/{product-id}/short")
    public ResponseEntity<ProductShortResponse> showShortProduct(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.findProductShortById(productId));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> showProduct(
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
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(request));
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

//    @PreAuthorize("hasAnyAuthority('MICROSERVICE')")
    @PostMapping("/short/batch")
    public ResponseEntity<List<ProductShortResponse>> showProducts(
            @RequestBody @Valid ProductBatchRequest request
    ) {
        return ResponseEntity.ok(productService.findProductShortByIds(request.ids()));
    }

//    @PreAuthorize("hasAnyAuthority('MICROSERVICE')")
    @PostMapping("/purchase")
    public ResponseEntity<ProductPurchaseResponse> purchaseProduct(
            @RequestBody @Valid ProductPurchaseRequest request
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

//    @PreAuthorize("hasAnyAuthority('MICROSERVICE')")
    @GetMapping("/{product-id}/exists")
    public ResponseEntity<Boolean> productExists(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.productExistsById(productId));
    }

}
