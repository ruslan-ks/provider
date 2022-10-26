package com.provider.util;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Only appends url parameters. Never replaces or removes them
 */
public class ParameterizedUrlImpl implements ParameterizedUrl {
    protected final Set<Pair<String, String>> params = new LinkedHashSet<>();

    private final String requestUri;

    public ParameterizedUrlImpl(@NotNull String url) {
        if (url.contains("?")) {
            final String[] urlParts =  url.split("[?]");
            requestUri = urlParts[0];

            final String[] urlParams = urlParts[1].split("&");
            Arrays.stream(urlParams)
                    .map(ParameterizedUrlImpl::splitParam)
                    .forEach(params::add);
        } else {
            requestUri = url;
        }

    }

    /**
     * Split param string and return pair of name and value.<br>
     * example: param = 'pageNumber=1'<br>
     * result: Pair of ('pageNumber', 1)
     * @param param parameter string
     * @return Pair of (name, value)
     */
    private static @NotNull Pair<String, String> splitParam(@NotNull String param) {
        final String[] paramNameAndValue = param.split("=");
        return Pair.of(paramNameAndValue[0], paramNameAndValue[1]);
    }

    public static ParameterizedUrlImpl of(@NotNull String url) {
        return new ParameterizedUrlImpl(url);
    }

    @Override
    public @NotNull String getString() {
        final String paramString = params.stream()
                .map(pair -> pair.getKey() + "=" + pair.getValue())
                .collect(Collectors.joining("&", "?", ""));
        return requestUri + paramString;
    }

    @Override
    public void addParam(@NotNull String paramName, @NotNull String paramValue) {
        params.add(Pair.of(paramName, paramValue));
    }

    @Override
    public void setParam(@NotNull String paramName, @NotNull String paramValue) {
        params.removeIf(p -> p.getKey().equals(paramName));
        params.add(Pair.of(paramName, paramValue));
    }

    @Override
    public String toString() {
        return "ParameterizedUrlImpl{" +
                "params=" + params +
                ", requestUri='" + requestUri + '\'' +
                '}';
    }
}
