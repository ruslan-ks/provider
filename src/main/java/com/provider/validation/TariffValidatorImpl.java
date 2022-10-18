package com.provider.validation;

import com.provider.constants.Regex;
import com.provider.util.Checks;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class TariffValidatorImpl implements TariffValidator {
    private static final Pattern regularTextPattern = Pattern.compile(Regex.REGULAR_TEXT);
    private static final Pattern latinTextPattern = Pattern.compile(Regex.FILE_NAME);

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
    public boolean isValidDuration(int months, long minutes) {
        return months >= 0 && minutes >= 0 && months + minutes >= 1;
    }

    @Override
    public boolean isValidImageFileName(@NotNull String fileName) {
        return latinTextPattern.matcher(fileName).matches();
    }
}
