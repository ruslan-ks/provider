package com.provider.validation;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class MoneyValidatorImpl implements MoneyValidator {
    @Override
    public boolean isPositiveAmount(@NotNull BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
