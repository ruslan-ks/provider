package com.provider.util;

import org.jetbrains.annotations.NotNull;

/**
 * Url with url parameters
 */
public interface ParameterizedUrl {
    /**
     * Returns url with url parameters
     * @return url with url parameters
     */
    @NotNull String getString();

    static ParameterizedUrl of(@NotNull String url) {
        return ParameterizedUrlImpl.of(url);
    }

    /**
     * Adds parameter name-value pair; If parameter by name of {@code paramName} has already been added,
     * adds one more parameter of the same name
     * @param paramName parameter name
     * @param paramValue parameter value
     */
    void addParam(@NotNull String paramName, @NotNull String paramValue);

    /**
     * Sets the specified url parameter; replaces the parameter by name of {@code paramName} if one is present
     * @param paramName parameter name
     * @param paramValue parameter value
     */
    void setParam(@NotNull String paramName, @NotNull String paramValue);
}
