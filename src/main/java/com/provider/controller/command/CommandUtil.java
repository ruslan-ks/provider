package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import com.provider.entity.Currency;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class
 */
public class CommandUtil {
    private CommandUtil() {}

    /**
     * Parse BigDecimal parameter
     * @param value value to be parsed
     * @return extracted BigDecimal valid
     * @throws CommandParamException if NumberFormatException occurred
     */
    public static @NotNull BigDecimal parseBigDecimalParam(@NotNull String value)
            throws CommandParamException {
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

    // TODO: javadoc
    public static int parseIntParam(@NotNull String value) throws CommandParamException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }

    public static List<Integer> parseIntParams(@NotNull List<String> values) throws CommandParamException {
        try {
            return values.stream().map(Integer::parseInt).toList();
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Failed to parse int parameter list: invalid number format: " + values);
        }
    }

    // TODO: javadoc
    public static long parseLongParam(@NotNull String value) throws CommandParamException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }

    public static @NotNull User.Status parseUserStatusParam(@NotNull String value) throws CommandParamException {
        try {
            return User.Status.valueOf(value);
        } catch(IllegalArgumentException ex) {
            throw new CommandParamException("User.Status cannot be parsed: invalid status: " + value);
        }
    }

    public static @NotNull User.Role parseUserRoleParam(@NotNull String value) throws CommandParamException {
        try {
            return User.Role.valueOf(value);
        } catch(IllegalArgumentException ex) {
            throw new CommandParamException("User.Role cannot be parsed: invalid role: " + value);
        }
    }

    public static @NotNull Tariff.Status parseTariffStatusParam(@NotNull String value) throws CommandParamException {
        try {
            return Tariff.Status.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new CommandParamException("Tariff.Status cannot be parsed: invalid status: " + value);
        }
    }
}
