package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import com.webstore.product_service.category.CategoryRepository;
import com.webstore.product_service.exception.ProductPurchaseException;
import com.webstore.product_service.exception.ProductValidateException;
import com.webstore.product_service.media.MediaClient;
import com.webstore.product_service.product.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductMapper productMapper;
    private final MediaClient mediaClient;

    @Override
    public Long createProduct(ProductRequest productRequest) {
        Category category = categoryRepo.findById(productRequest.categoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + productRequest.categoryId() + " not found")
        );
        if (productRepo.existsByName(productRequest.name())) {
            throw new ProductValidateException("Product with name " + productRequest.name() + " already exists");
        }
        Product product = productMapper.toProduct(productRequest, category);
        product.setCreated(LocalDateTime.now());

        Product savedProduct = productRepo.save(product);

        if (productRequest.thumbnail() != null) {
            String thumbnailImageId = mediaClient.uploadImage(productRequest.thumbnail());
            savedProduct.setThumbnailImageId(thumbnailImageId);
        }

        if (productRequest.gallery() != null) {
            List<String> galleryImageIds = mediaClient.uploadImages(productRequest.gallery());
            savedProduct.setGalleryImageIds(galleryImageIds);
        }

        if (productRequest.thumbnail() != null || productRequest.gallery() != null) {
            productRepo.save(savedProduct);
        }

        return savedProduct.getId();
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request) {
        var product = productRepo.findById(request.id()).orElseThrow(
                () -> new EntityNotFoundException("Product with id" + request.id() + " not found.")
        );
        var category = categoryRepo.findById(request.categoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + request.categoryId() + " not found")
        );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setCategory(category);
        
        if (request.thumbnail() != null) {
            mediaClient.deleteImage(product.getThumbnailImageId());
            String thumbnailImageId = mediaClient.uploadImage(request.thumbnail());
            product.setThumbnailImageId(thumbnailImageId);
        }

        if (request.gallery() != null) {
            mediaClient.deleteImages(product.getGalleryImageIds());
            List<String> galleryImageIds = mediaClient.uploadImages(request.gallery());
            product.setGalleryImageIds(galleryImageIds);
        }

        return productMapper.toProductResponse(productRepo.save(product));
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = findProductById(id);

        if (product.getThumbnailImageId() != null) {
            mediaClient.deleteImage(product.getThumbnailImageId());
        }

        if (product.getGalleryImageIds() != null) {
            mediaClient.deleteImages(product.getGalleryImageIds());
        }

        productRepo.deleteById(id);
    }

    @Override
    public ProductShortResponse getProductShortResponseById(Long id) {
        return productMapper.toProductShortResponse(findProductById(id));
    }

    @Override
    public ProductResponse getProductResponseById(Long id) {
        return productMapper.toProductResponse(findProductById(id));
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
                .map(ProductPurchaseItemRequest::productId)
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
                    return productMapper.toProductPurchaseItemResponse(product, req.quantity());
                })
                .toList()
        );
    }

    @Override
    public List<ProductShortResponse> getProductShortResponseByIds(List<Long> ids) {
        return productRepo.findAllByIdInWithLock(ids)
                .stream()
                .map(productMapper::toProductShortResponse)
                .toList();
    }

    private Product findProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found."));
    }
}
