package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.ProductPurchaseRequest;
import com.webstore.product_service.product.dto.ProductPurchaseResponse;
import com.webstore.product_service.product.dto.ProductRequest;
import com.webstore.product_service.product.dto.ProductShortResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductShortResponse>> findAll() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        productService.updateProduct(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("product-id") Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductShortResponse> findProduct(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping("/{product-id}/exists")
    public ResponseEntity<Boolean> productExists(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(productService.productExistsById(productId));
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
