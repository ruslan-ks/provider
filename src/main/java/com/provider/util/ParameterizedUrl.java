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
        return AppendingParameterizedUrl.of(url);
    }

    void addParam(@NotNull String paramName, @NotNull String paramValue);
}
