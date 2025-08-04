package com.webstore.productservice.product;

import com.webstore.productservice.product.dto.ProductPurchaseRequest;
import com.webstore.productservice.product.dto.ProductPurchaseResponse;
import com.webstore.productservice.product.dto.ProductRequest;
import com.webstore.productservice.product.dto.ProductResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(this.productService.findAllProducts());
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        return ResponseEntity.ok(this.productService.createProduct(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(
            @RequestBody @Valid ProductRequest request
    ) {
        this.productService.updateProduct(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("product-id") Long productId
    ) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findProduct(
            @PathVariable("product-id") Long productId
    ) {
        return ResponseEntity.ok(this.productService.findProductById(productId));
    }

    // Deleting products from db
    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProduct(
            @RequestBody @Valid List<ProductPurchaseRequest> request
    ) {
        return ResponseEntity.ok(this.productService.purchaseProducts(request));
    }

}
