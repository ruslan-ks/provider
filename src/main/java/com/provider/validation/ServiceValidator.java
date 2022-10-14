package com.provider.validation;

import org.jetbrains.annotations.NotNull;

public interface ServiceValidator {
    /**
     * Returns true if name is valid
     * @param name name to be checked
     * @return true if name is valid, false otherwise
     */
    boolean isValidName(@NotNull String name);

    /**
     * Returns true if description is valid
     * @param description description to be checked
     * @return true if description is valid, false otherwise
     */
    boolean isValidDescription(@NotNull String description);
}
