package com.provider.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Url with url parameters
 */
public interface ParameterizedUrl {
    /**
     * Returns url with url parameters
     * @return url with url parameters
     */
    @NotNull String getString();

    static ParameterizedUrl of(@NotNull String url, @NotNull Map<String, String> map) {
        return AppendingParameterizedUrl.of(url, map);
    }
}
