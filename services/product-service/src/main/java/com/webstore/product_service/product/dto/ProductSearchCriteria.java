package com.webstore.product_service.product.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {
    private List<Long> ids;
    private String name;
    private List<Long> categoryIds;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Long quantity;
}