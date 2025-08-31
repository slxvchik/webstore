package com.webstore.review_service.exceptions;

public class ReviewNotFoundException extends ReviewException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
