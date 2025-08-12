package com.webstore.auth_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class UserValidateException extends RuntimeException {
    private final String msg;
}
