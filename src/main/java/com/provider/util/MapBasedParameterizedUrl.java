package com.provider.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class MapBasedParameterizedUrl implements ParameterizedUrl {
    protected final Map<String, String> paramMap;
    protected final String url;

    public MapBasedParameterizedUrl(@NotNull String url, @NotNull Map<String, String> paramMap) {
        this.url = url;
        this.paramMap = Map.copyOf(paramMap);
    }

    public static MapBasedParameterizedUrl of(@NotNull String url, @NotNull Map<String, String> paramMap) {
        return new MapBasedParameterizedUrl(url, paramMap);
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
