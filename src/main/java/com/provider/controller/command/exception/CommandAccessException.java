package com.provider.controller.command.exception;

public class CommandAccessException extends FrontCommandException {
    public CommandAccessException() {}

    public CommandAccessException(String message) {
        super(message);
    }

    public CommandAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandAccessException(Throwable cause) {
        super(cause);
    }
}
