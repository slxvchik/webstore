package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public record ProductSearchRequest(
    @RequestParam(value = "ids", required = false)
    @Max(100)
    List<Long> ids,
    @RequestParam(value = "name", required = false)
    String name,
    @RequestParam(value = "categoryIds", required = false)
    @Max(100)
    List<Long> categoryIds,
    @RequestParam(value = "minPrice", required = false)
    @Positive
    BigDecimal minPrice,
    @RequestParam(value = "maxPrice", required = false)
    @Positive
    BigDecimal maxPrice,
    @RequestParam(value = "quantity", required = false)
    Boolean quantity,
    @RequestParam(value = "page", defaultValue = "1")
    @Min(1)
    int page,
    @RequestParam(value = "size", defaultValue = "20")
    @Min(1) @Max(100)
    int size,
    @RequestParam(value = "sort", defaultValue = "created,desc")
    String sort
) {}
