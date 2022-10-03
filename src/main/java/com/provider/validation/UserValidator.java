package com.provider.validation;

import org.jetbrains.annotations.NotNull;

/**
 * User data validator.
 * Used to check user data when adding new user.
 * Checks whether user properties meet their requirements conditions
 */
public interface UserValidator {
    /**
     * Returns true if login is valid
     * @param login user login
     * @return true if login is valid
     */
    boolean isValidLogin(@NotNull String login);

    /**
     * Checks if password is valid
     * @param password user password
     * @return true if password is valid
     */
    boolean isValidPassword(@NotNull String password);

    /**
     * Checks if name is valid
     * @param name user first name
     * @return true if name is valid
     */
    boolean isValidName(@NotNull String name);

    /**
     * Checks if surname is valid
     * @param surname user surname
     * @return true if surname is valid
     */
    boolean isValidSurname(@NotNull String surname);

    /**
     * Checks if phone is valid
     * @param phone user phone
     * @return true if phone is valid
     */
    boolean isValidPhone(@NotNull String phone);
}
