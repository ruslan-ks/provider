package com.provider.validation;

import org.jetbrains.annotations.NotNull;

public class ValidatorFactoryImpl implements ValidatorFactory {
    private ValidatorFactoryImpl() {}

    private static final UserValidator userValidator = new UserValidatorImpl();
    private static final ServiceValidator serviceValidator = new ServiceValidatorImpl();
    private static final TariffValidator tariffValidator = new TariffValidatorImpl();
    private static final MoneyValidator moneyValidator = new MoneyValidatorImpl();

    public static ValidatorFactoryImpl newInstance() {
        return new ValidatorFactoryImpl();
    }

    @Override
    public @NotNull UserValidator getUserValidator() {
        return userValidator;
    }

    @Override
    public @NotNull ServiceValidator getServiceValidator() {
        return serviceValidator;
    }

    @Override
    public @NotNull TariffValidator getTariffValidator() {
        return tariffValidator;
    }

    @Override
    public @NotNull MoneyValidator getMoneyValidator() {
        return moneyValidator;
    }
}
