package com.provider.service.exception;

public class InvalidPropertyException extends Exception {
    public InvalidPropertyException() {
        super();
    }

    public InvalidPropertyException(String message) {
        super(message);
    }

    public InvalidPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPropertyException(Throwable cause) {
        super(cause);
    }
}
