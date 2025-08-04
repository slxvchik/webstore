package com.webstore.productservice.handler;

import com.webstore.productservice.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CategoryValidateException.class)
    public ResponseEntity<String> handleEmailAndUsernameException(CategoryValidateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMsg());
    }

    @ExceptionHandler(ProductValidateException.class)
    public ResponseEntity<String> handleProductValidateException(ProductValidateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMsg());
    }

    @ExceptionHandler(ProductPurchaseException.class)
    public ResponseEntity<String> handleProductPurchaseException(ProductPurchaseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
