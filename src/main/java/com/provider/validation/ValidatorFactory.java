package com.provider.validation;

import org.jetbrains.annotations.NotNull;

/**
 * Creates different validator classes for entity data validation
 */
public interface ValidatorFactory {
    /**
     * Returns UserValidator object
     * @return UserValidator
     */
    @NotNull UserValidator getUserValidator();

    /**
     * Returns ServiceValidator object
     * @return ServiceValidator object
     */
    @NotNull ServiceValidator getServiceValidator();

    /**
     * Returns TariffValidator object
     * @return TariffValidator object
     */
    @NotNull TariffValidator getTariffValidator();
}
