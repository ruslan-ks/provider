package com.provider.util;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Only appends url parameters. Never replaces or removes them
 */
public class AppendingParameterizedUrl implements ParameterizedUrl {
    protected final List<Pair<String, String>> params = new ArrayList<>();
    protected final String url;

    public AppendingParameterizedUrl(@NotNull String url) {
        this.url = url;
    }

    public static AppendingParameterizedUrl of(@NotNull String url) {
        return new AppendingParameterizedUrl(url);
    }

    @Override
    public @NotNull String getString() {
        final String paramPrefix = url.contains("?") ? "&" : "?";
        final String paramString = params.stream()
                .map(pair -> pair.getKey() + "=" + pair.getValue())
                .collect(Collectors.joining("&", paramPrefix, ""));
        return url + paramString;
    }

    @Override
    public void addParam(@NotNull String paramName, @NotNull String paramValue) {
        params.add(Pair.of(paramName, paramValue));
    }

    @Override
    public String toString() {
        return "AppendingParameterizedUrl{" +
                "params=" + params +
                ", url='" + url + '\'' +
                '}';
    }
}
