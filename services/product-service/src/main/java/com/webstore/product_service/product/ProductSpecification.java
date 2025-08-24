package com.webstore.product_service.product;

import com.webstore.product_service.product.dto.ProductSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductSpecification {

    public static Specification<Product> hasIds(List<Long> ids) {
        return (root, query, cb) ->
                ids == null || ids.isEmpty() ? null : root.get("id").in(ids);
    }
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty() ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> hasCategoryIds(List<Long> categoryIds) {
        return (root, query, cb) ->
                categoryIds == null || categoryIds.isEmpty() ? null : root.get("category_id").in(categoryIds);
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> quantity(Long quantity) {
        return (root, query, cb) ->
                quantity == null ? null : cb.greaterThanOrEqualTo(root.get("quantity"), quantity);
    }

    public static Specification<Product> buildSpecification(ProductSearchCriteria criteria) {
        return Specification.allOf(hasName(criteria.getName()))
                .and(hasIds(criteria.getIds()))
                .and(hasCategoryIds(criteria.getCategoryIds()))
                .and(priceGreaterThanOrEqual(criteria.getMinPrice()))
                .and(priceLessThanOrEqual(criteria.getMaxPrice()))
                .and(quantity(criteria.getQuantity()));
    }
}
