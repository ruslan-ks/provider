package com.provider.controller.upload;

public class InvalidMimeTypeException extends Exception {
    public InvalidMimeTypeException() {
        super();
    }

    public InvalidMimeTypeException(String message) {
        super(message);
    }

    public InvalidMimeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMimeTypeException(Throwable cause) {
        super(cause);
    }
}
