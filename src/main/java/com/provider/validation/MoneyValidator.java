package com.provider.validation;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface MoneyValidator {
    boolean isPositiveAmount(@NotNull BigDecimal amount);
}
