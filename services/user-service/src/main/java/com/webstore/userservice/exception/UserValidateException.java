package com.webstore.userservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class UserValidateException extends RuntimeException {
    private final String msg;
}
