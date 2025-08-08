package com.webstore.product_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class CategoryValidateException extends RuntimeException {
    private final String msg;
}
