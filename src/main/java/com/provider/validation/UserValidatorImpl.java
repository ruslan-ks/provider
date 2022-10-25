package com.provider.validation;

import com.provider.constants.Regex;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class UserValidatorImpl implements UserValidator {
    UserValidatorImpl() {}

    private static final Pattern loginPattern = Pattern.compile(Regex.LOGIN);

    // used for both name and surname matching
    private static final Pattern namePattern = Pattern.compile(Regex.NAME);

    private static final Pattern phonePattern = Pattern.compile(Regex.PHONE);

    @Override
    public boolean isValidLogin(@NotNull String login) {
        return loginPattern.matcher(login).matches();
    }

    @Override
    public boolean isValidPassword(@NotNull String password) {
        return password.length() >= 4;
    }

    @Override
    public boolean isValidName(@NotNull String name) {
       return namePattern.matcher(name).matches();
    }

    @Override
    public boolean isValidSurname(@NotNull String surname) {
        return namePattern.matcher(surname).matches();
    }

    @Override
    public boolean isValidPhone(@NotNull String phone) {
        return phonePattern.matcher(phone).matches();
    }
}
