package com.webstore.productservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class ProductValidateException extends RuntimeException {
    private final String msg;
}
