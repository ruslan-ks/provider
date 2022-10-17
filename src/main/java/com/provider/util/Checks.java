package com.provider.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class Checks {
    private Checks() {}

    public static boolean isGreaterEqualZero(@NotNull BigDecimal value) {
        return !(value.compareTo(BigDecimal.ZERO) < 0);
    }
}
