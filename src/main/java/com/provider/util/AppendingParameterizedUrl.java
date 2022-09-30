package com.provider.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Only appends url parameters. Never replaces or removes them
 */
public class AppendingParameterizedUrl implements ParameterizedUrl {
    protected final Map<String, String> paramMap;
    protected final String url;

    public AppendingParameterizedUrl(@NotNull String url, @NotNull Map<String, String> paramMap) {
        this.url = url;
        this.paramMap = Map.copyOf(paramMap);
    }

    public static AppendingParameterizedUrl of(@NotNull String url, @NotNull Map<String, String> paramMap) {
        return new AppendingParameterizedUrl(url, paramMap);
    }

    @Override
    public @NotNull String getString() {
        final String paramString = paramMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        final String paramPrefix = url.contains("?") ? "&" : "?";
        return url + paramPrefix + paramString;
    }

    @Override
    public String toString() {
        return "MapBasedParameterizedUrl{" +
                "paramMap=" + paramMap +
                ", url='" + url + '\'' +
                '}';
    }
}
