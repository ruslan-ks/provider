package com.provider.controller.command;

/**
 * Command interface used by the front controller servlet.
 */
public interface FrontCommand {
    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     */
    void execute();
}
