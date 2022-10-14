package com.provider.validation;

import com.provider.constants.Regex;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ServiceValidatorImpl implements ServiceValidator {
    private final static Pattern regularTextPattern = Pattern.compile(Regex.REGULAR_TEXT);

    ServiceValidatorImpl() {}

    @Override
    public boolean isValidName(@NotNull String name) {
        return regularTextPattern.matcher(name).matches();
    }

    @Override
    public boolean isValidDescription(@NotNull String description) {
        return regularTextPattern.matcher(description).matches();
    }
}
