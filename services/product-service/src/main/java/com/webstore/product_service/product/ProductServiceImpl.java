package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryRepository;
import com.webstore.product_service.exception.ProductPurchaseException;
import com.webstore.product_service.exception.ProductValidateException;
import com.webstore.product_service.product.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;

    @Override
    public List<ProductShortResponse> findAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(productMapper::toShortProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long createProduct(ProductRequest productRequest) {
        Category category = categoryRepo.findById(productRequest.categoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + productRequest.categoryId() + " not found")
        );
        if (productRepo.existsByName(productRequest.name())) {
            throw new ProductValidateException("Product with name " + productRequest.name() + " already exists");
        }
        Product product = productMapper.toProduct(productRequest, category);
        return productRepo.save(product).getId();
    }

    @Override
    public void updateProduct(ProductRequest request) {
        var product = productRepo.findById(request.id()).orElseThrow(
                () -> new EntityNotFoundException("Product with id" + request.id() + " not found.")
        );
        var category = categoryRepo.findById(request.categoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + request.categoryId() + " not found")
        );
        productsMerge(product, request, category);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product with id: " + id + " not found.");
        }
        productRepo.deleteById(id);
    }

    @Override
    public ProductShortResponse findProductById(Long id) {
        return productRepo.findById(id)
                .map(productMapper::toShortProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found."));
    }

    @Override
    public ProductShortResponse findProductByName(String name) {
        return productRepo.findByName(name)
                .map(productMapper::toShortProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with name: " + name + " not found."));
    }

    @Override
    public boolean productExistsById(Long id) {
        return productRepo.existsById(id);
    }

    @Transactional(rollbackOn = ProductPurchaseException.class)
    @Override
    public ProductPurchaseResponse purchaseProducts(ProductPurchaseRequest productsRequest) {

        // check missed ids
        var productIds = productsRequest.products().stream()
                .map(ProductPurchaseItem::productId)
                .toList();
        var foundProducts = productRepo.findAllByIdInWithLock(productIds);

        if (productIds.size() != foundProducts.size()) {
            var foundIds = foundProducts.stream()
                    .map(Product::getId)
                    .toList();
            var missedIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new ProductPurchaseException("These products ids don't found:" + missedIds);
        }

        // map for products
        var productMap = foundProducts
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // quantity valid (all in stock)
        List<String> errors = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (var productRequest : productsRequest.products()) {
            var product = productMap.get(productRequest.productId());
            if (product.getQuantity() - productRequest.quantity() < 0) {
                errors.add("Insufficient stock quantity for product with ID:" + product.getId() + ".");
            }
            if (!product.getPrice().equals(productRequest.price())) {
                errors.add("Incorrect price for product with ID: " + product.getId() + ".");
            }
            totalPrice = totalPrice.add(product.getPrice());
        }

        if (!errors.isEmpty()) {
            throw new ProductPurchaseException(String.join(" ", errors));
        }

        for (var productRequest : productsRequest.products()) {
            productRepo.decreaseStock(productRequest.productId(), productRequest.quantity());
        }

        return new ProductPurchaseResponse(
            totalPrice,
            productsRequest.products().stream()
                .map(req -> {
                    var product = productMap.get(req.productId());
                    return productMapper.toProductPurchaseItem(product, req.quantity());
                })
                .toList()
        );
    }

    private void productsMerge(Product product, ProductRequest productRequest, Category newCategory) {
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setQuantity(productRequest.quantity());
        product.setCategory(newCategory);
    }
}
