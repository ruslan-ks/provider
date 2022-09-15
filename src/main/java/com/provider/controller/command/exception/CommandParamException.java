package com.provider.controller.command.exception;

public class CommandParamException extends FrontCommandException {
    public CommandParamException() {
        super();
    }

    public CommandParamException(String message) {
        super(message);
    }

    public CommandParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandParamException(Throwable cause) {
        super(cause);
    }
}
