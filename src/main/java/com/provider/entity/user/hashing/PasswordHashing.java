package com.provider.entity.user.hashing;

import com.provider.entity.user.UserPassword;
import org.jetbrains.annotations.NotNull;

public interface PasswordHashing {
    /**
     * Password hashing algorithms.
     * Suffix _X (for example: _1) specifies internal implementation version(for this app).
     */
    enum HashMethod {
        /**
         * 1-st version(for this app) of PBKDF2 algorithm implementation
         */
        PBKDF2_1
    }

    /**
     * Provides with new implementation object according to HashMethod
     * @param hashMethod method of hashing
     * @return PasswordHashing object
     */
    static PasswordHashing getInstance(HashMethod hashMethod) {
        // Can be replaced with switch-case in the future
        if (hashMethod == HashMethod.PBKDF2_1) {
            return Pbkdf2PasswordHashing.getInstance();
        }
        throw new IllegalArgumentException();
    }

    /**
     * Used to generate hashed password when checking password correctness.
     * @param password user entered password
     * @param salt salt that will be used to hash a password
     * @return hashed password with provided salt
     * @throws IllegalArgumentException if password is blank
     */
    @NotNull UserPassword hash(@NotNull String password, @NotNull String salt);

    /**
     * Should be used when registering a user and hashing his password for the first time.
     * @param password user entered password
     * @return hashed password with generated salt used to hash it
     * @throws IllegalArgumentException if password is blank
     */
    @NotNull UserPassword hash(@NotNull String password);
}
