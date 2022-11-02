package com.provider.controller.command.exception;

public class UserAccessRightsException extends FrontCommandException {
    public UserAccessRightsException() {
    }

    public UserAccessRightsException(String message) {
        super(message);
    }

    public UserAccessRightsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccessRightsException(Throwable cause) {
        super(cause);
    }
}
