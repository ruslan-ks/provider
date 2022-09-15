package com.provider.controller.command.exception;

public class FrontCommandException extends Exception {
    public FrontCommandException() {
        super();
    }

    public FrontCommandException(String message) {
        super(message);
    }

    public FrontCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrontCommandException(Throwable cause) {
        super(cause);
    }
}
