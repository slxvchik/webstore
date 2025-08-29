package com.webstore.product_service.product.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductBatchRequest(
        @Size(min = 1, max = 100) List<Long> ids
) {}