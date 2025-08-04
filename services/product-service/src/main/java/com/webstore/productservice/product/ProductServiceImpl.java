package com.webstore.productservice.product;

import com.webstore.productservice.category.Category;
import com.webstore.productservice.category.CategoryRepository;
import com.webstore.productservice.exception.ProductPurchaseException;
import com.webstore.productservice.exception.ProductValidateException;
import com.webstore.productservice.product.dto.ProductPurchaseRequest;
import com.webstore.productservice.product.dto.ProductPurchaseResponse;
import com.webstore.productservice.product.dto.ProductRequest;
import com.webstore.productservice.product.dto.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> findAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(productMapper::toProductResponse)
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
    public ProductResponse findProductById(Long id) {
        return productRepo.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found."));
    }

    @Override
    public ProductResponse findProductByName(String name) {
        return productRepo.findByName(name)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product with name: " + name + " not found."));
    }

    @Transactional(rollbackOn = ProductPurchaseException.class)
    @Override
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests) {

        // check missed ids
        var productIds = requests.stream()
                .map(ProductPurchaseRequest::id)
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
        for (var request : requests) {
            var product = productMap.get(request.id());
            if (product.getQuantity() - request.quantity() < 0) {
                errors.add("Insufficient stock quantity for product with ID:" + product.getId() + ".");
            }
        }

        if (!errors.isEmpty()) {
            throw new ProductPurchaseException(String.join(" ", errors));
        }

        for (var request : requests) {
            productRepo.decreaseStock(request.id(), request.quantity());
        }

        return requests.stream()
                .map(req -> {
                    var product = productMap.get(req.id());
                    return productMapper.toproductPurchaseResponse(product, req.quantity());
                })
                .toList();
    }

    private void productsMerge(Product product, ProductRequest productRequest, Category newCategory) {
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setQuantity(productRequest.quantity());
        product.setCategory(newCategory);
    }
}
