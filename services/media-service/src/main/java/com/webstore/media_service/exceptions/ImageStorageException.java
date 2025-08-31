package com.webstore.media_service.exceptions;

public class ImageStorageException extends ImageException {
    public ImageStorageException(String message) { super(message); }
    public ImageStorageException(String message, Throwable cause) { super(message, cause); }
}
