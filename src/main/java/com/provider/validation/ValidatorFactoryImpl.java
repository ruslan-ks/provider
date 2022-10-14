package com.provider.validation;

import org.jetbrains.annotations.NotNull;

public class ValidatorFactoryImpl implements ValidatorFactory {
    private ValidatorFactoryImpl() {}

    public static ValidatorFactoryImpl newInstance() {
        return new ValidatorFactoryImpl();
    }

    @Override
    public @NotNull UserValidator getUserValidator() {
        return new UserValidatorImpl();
    }

    @Override
    public @NotNull ServiceValidator getServiceValidator() {
        return new ServiceValidatorImpl();
    }
}
