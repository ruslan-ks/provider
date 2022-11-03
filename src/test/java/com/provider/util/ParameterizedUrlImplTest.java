package com.provider.util;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParameterizedUrlImplTest {
    @ParameterizedTest
    @MethodSource({"com.provider.util.ParameterizedUrlTest#simpleUrlStream",
            "com.provider.util.ParameterizedUrlTest#urlWithParamsStream"})
    public void testAddParam(String value) {
        final var url = ParameterizedUrl.of(value);
        paramPairStream()
                .peek(p -> url.addParam(p.getLeft(), p.getRight()))
                .forEach(p -> assertTrue(url.getString().contains(p.getLeft() + "=" + p.getRight())));
    }

    @ParameterizedTest
    @MethodSource({"com.provider.util.ParameterizedUrlTest#simpleUrlStream",
            "com.provider.util.ParameterizedUrlTest#urlWithParamsStream"})
    public void testSetParam(String value) {
        final String oldValue = "INVALID VALUE";
        final var url = ParameterizedUrl.of(value);
        paramPairStream()
                .peek(p -> url.addParam(p.getLeft(), oldValue))
                .peek(p -> url.setParam(p.getLeft(), p.getRight()))
                .forEach(p -> {
                    assertTrue(url.getString().contains(paramString(p)));
                    assertFalse(url.getString().contains(paramString(p.getLeft(), oldValue)));  // no old value
                });
    }

    private static Stream<Pair<String, String>> paramPairStream() {
        return IntStream.iterate(0, (i) -> ++i)
                .limit(10)
                .mapToObj(i -> Pair.of("testParam" + i, "value" + i));
    }

    private static String paramString(Pair<String, String> paramPair) {
        return paramString(paramPair.getLeft(), paramPair.getRight());
    }

    private static String paramString(String name, String value) {
        return name + "=" + value;
    }
}