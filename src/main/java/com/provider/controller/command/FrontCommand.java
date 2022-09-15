package com.provider.controller.command;

import com.provider.dao.exception.DBException;
import jakarta.servlet.ServletException;

import java.io.IOException;

/**
 * Command interface used by the front controller servlet.
 */
public interface FrontCommand {
    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     */
    void execute() throws DBException, ServletException, IOException;
}
