package com.provider.validation;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface TariffValidator {
    boolean isValidTitle(@NotNull String title);
    boolean isValidDescription(@NotNull String description);
    boolean isValidUsdPrice(@NotNull BigDecimal usdPrice);
    boolean isValidDuration(int months, int minutes);
}
