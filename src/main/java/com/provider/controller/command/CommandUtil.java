package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import com.provider.entity.Currency;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Utility class
 */
public class CommandUtil {
    private CommandUtil() {}

    /**
     * Parse BigDecimal parameter
     * @param value value to be parsed
     * @return extracted BigDecimal valud
     * @throws CommandParamException if NumberFormatException occurred
     */
    public static @NotNull BigDecimal parseBigDecimalParam(@NotNull String value) throws CommandParamException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: value = " + value);
        }
    }

    /**
     * Parse currency parameter - throws if parsing fails
     * @param currency currency name
     * @return Currency object obtained via <code>Currency.valueOf(name)</code>
     * @throws CommandParamException if param currency cannot be passed(IllegalArgumentException occurred during
     * the parsing)
     */
    public static @NotNull Currency parseCurrencyParam(@NotNull String currency) throws CommandParamException {
        try {
            return Currency.valueOf(currency);
        } catch(IllegalArgumentException ex) {
            throw new CommandParamException("Currency cannot be parsed - invalid currency: '" + currency + "'");
        }
    }

    public static int parseIntParam(@NotNull String value) throws CommandParamException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }
}
