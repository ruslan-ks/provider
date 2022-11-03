package com.provider.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ChecksTest {

    @ParameterizedTest
    @MethodSource("greaterEqualZeroValuesStream")
    void testIsGreaterEqualZero(BigDecimal value) {
        assertTrue(Checks.isGreaterEqualZero(value));
    }

    @ParameterizedTest
    @MethodSource("greaterEqualZeroValuesStream")
    void testIsLessThanZero(BigDecimal value) {
        assertFalse(Checks.isLessThanZero(value));
    }

    @Test
    void testThrowIfInvalidId() {
        final Stream<Long> validIds = Stream.of(1L, 2L, 3L, 4L, (long) Integer.MAX_VALUE, Long.MAX_VALUE);
        validIds.forEach(id -> assertDoesNotThrow(() -> Checks.throwIfInvalidId(id)));
        final Stream<Long> invalidIds = Stream.of(0L, -1L, -2L, -3L, (long) Integer.MIN_VALUE, Long.MIN_VALUE);
        invalidIds.forEach(id -> assertThrows(IllegalArgumentException.class, () -> Checks.throwIfInvalidId(id)));
    }

    @Test
    void testThrowIfInvalidOffsetOrLimitThrows() {
        final List<Long> invalidOffsets = List.of(-1L, -2L, -3L, (long) Integer.MIN_VALUE, Long.MIN_VALUE);
        final List<Integer> invalidLimits = List.of(0, -1, -2, -3, Integer.MIN_VALUE);
        for (var offset : invalidOffsets) {
            for (var limit : invalidLimits) {
                // both illegal
                assertThrows(IllegalArgumentException.class, () -> Checks.throwIfInvalidOffsetOrLimit(offset, limit));
            }

            // illegal offset, legal limit
            assertThrows(IllegalArgumentException.class, () -> Checks.throwIfInvalidOffsetOrLimit(offset, 1));
        }
        for (var limit : invalidLimits) {
            // legal offset, illegal limit
            assertThrows(IllegalArgumentException.class, () -> Checks.throwIfInvalidOffsetOrLimit(1, limit));
        }
    }

    @Test
    void testThrowIfInvalidOffsetOrLimitDoesNotThrow() {
        final List<Long> legalOffsets = List.of(0L, 1L, 2L, 3L, (long) Integer.MAX_VALUE, Long.MAX_VALUE);
        final List<Integer> legalLimits = List.of(1, 2, 3, Integer.MAX_VALUE);
        for (var offset : legalOffsets) {
            for (var limit : legalLimits) {
                assertDoesNotThrow(() -> Checks.throwIfInvalidOffsetOrLimit(offset, limit));
            }
        }
    }

    public static Stream<BigDecimal> greaterEqualZeroValuesStream() {
        return Stream.of(
                BigDecimal.ZERO,
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.valueOf(Integer.MAX_VALUE)
        );
    }
}