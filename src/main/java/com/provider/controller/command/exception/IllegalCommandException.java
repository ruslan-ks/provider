package com.provider.controller.command.exception;

public class IllegalCommandException extends FrontCommandException {
    public IllegalCommandException() {
        super();
    }

    public IllegalCommandException(String message) {
        super(message);
    }

    public IllegalCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCommandException(Throwable cause) {
        super(cause);
    }
}
