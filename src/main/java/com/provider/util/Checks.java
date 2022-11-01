package com.provider.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class Checks {
    private Checks() {}

    public static boolean isGreaterEqualZero(@NotNull BigDecimal value) {
        return !(isLessThanZero(value));
    }

    public static boolean isLessThanZero(@NotNull BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Throws if {@code id <= 0}
     * @param id id to be checked
     * @throws IllegalArgumentException if {@code id <= 0}
     */
    public static void throwIfInvalidId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
    }

    /**
     * Throws IllegalArgumentException if offset or limit are invalid
     * @param offset offset to be checked
     * @param limit limit to be checked
     */
    public static void throwIfInvalidOffsetOrLimit(long offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("Invalid page: offset: " + offset + ", limit: " + limit);
        }
    }
}
