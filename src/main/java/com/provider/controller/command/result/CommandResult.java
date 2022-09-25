package com.provider.controller.command.result;

import org.jetbrains.annotations.NotNull;

/**
 * Command execution result
 * Provides an ability for controller servlet to return command-chosen view
 */
public interface CommandResult {
    /**
     * Returns resulting page location
     */
    @NotNull String getViewLocation();
}
