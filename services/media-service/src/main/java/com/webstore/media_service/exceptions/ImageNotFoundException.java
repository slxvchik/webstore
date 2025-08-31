package com.webstore.media_service.exceptions;

public class ImageNotFoundException extends ImageException {
    public ImageNotFoundException(String message) { super(message); }
    public ImageNotFoundException(String message, Throwable cause) { super(message, cause); }
}
