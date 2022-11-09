package com.provider.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockUtil {
    private MockUtil() {}

    public static @NotNull HttpServletRequest mockRequestWithSession(@NotNull HttpSession session) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        return request;
    }

    public static @NotNull HttpServletRequest mockRequestWithParams(@NotNull Map<String, String> paramMap) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        mockAllGetParameterMethods(request, paramMap);
        return request;
    }

    public static void mockAllGetParameterMethodsMulti(@NotNull HttpServletRequest request,
                                                  @NotNull Map<String, String[]> paramMap) {
        when(request.getParameterMap()).thenReturn(Map.copyOf(paramMap));
        paramMap.forEach((name, values) -> {
            when(request.getParameterValues(name)).thenReturn(values);
            when(request.getParameter(name)).thenReturn(values[0]);
        });
    }

    public static void mockAllGetParameterMethods(@NotNull HttpServletRequest request,
                                                  @NotNull Map<String, String> paramMap) {
        final Map<String, String[]> multiValueParamMap = paramMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), new String[]{e.getValue()}))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        mockAllGetParameterMethodsMulti(request, multiValueParamMap);
    }
}
