package com.provider.validation;

import com.provider.constants.Regex;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class TariffValidatorImpl implements TariffValidator {
    private static final Pattern regularTextPattern = Pattern.compile(Regex.REGULAR_TEXT);

    @Override
    public boolean isValidTitle(@NotNull String title) {
        return regularTextPattern.matcher(title).matches();
    }

    @Override
    public boolean isValidDescription(@NotNull String description) {
        return regularTextPattern.matcher(description).matches();
    }

    @Override
    public boolean isValidUsdPrice(@NotNull BigDecimal usdPrice) {
        return Checks.isGreaterEqualZero(usdPrice);
    }

    @Override
    public boolean isValidDuration(int months, int minutes) {
        return months >= 0 && minutes >= 0 && months + minutes >= 1;
    }
}
