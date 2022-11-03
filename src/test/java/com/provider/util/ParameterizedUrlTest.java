package com.provider.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParameterizedUrlTest {
    @ParameterizedTest
    @MethodSource({"simpleUrlStream", "urlWithParamsStream"})
    public void testOf(String value) {
        final var url = ParameterizedUrl.of(value);
        assertEquals(value, url.getString());
    }

    public static Stream<String> simpleUrlStream() {
        return Stream.of(
                "http://127.0.0.1/provider",
                "http://127.0.0.1/provider/index.jsp",
                "http://127.0.0.1/provider/controller",
                "http://localhost/provider/page",
                "/provider/controller",
                "/provider",
                "something"
        );
    }

    public static Stream<String> urlWithParamsStream() {
        return Stream.of(
                "http://127.0.0.1/provider?param1=42",
                "http://127.0.0.1/provider/index.jsp?param1=hello&param2=555",
                "http://127.0.0.1/provider/controller?id=1&name=Peter",
                "http://localhost/provider/page?pageNumber=15&pageSize=5",
                "/provider/controller?command=commandPlaceholder&services=internet&services=TV&services=magic",
                "/provider?user=Peter",
                "something?param1=doIt&param2=JustDoIt&param3=777"
        );
    }
}