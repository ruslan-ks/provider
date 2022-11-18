package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import com.provider.entity.Currency;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * Parses integer value; throws {@link CommandParamException} if value cannot be parsed
     * @param value value to be parsed
     * @return parsed integer value
     * @throws CommandParamException if value cannot be parsed
     */
    public static int parseIntParam(@NotNull String value) throws CommandParamException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }

    /**
     * Parses integer value; throws {@link CommandParamException} if value cannot be parsed
     * @param values values to be parsed
     * @return parsed values set
     * @throws CommandParamException if at least one value cannot be parsed
     */
    public static Set<Integer> parseIntParams(@NotNull Collection<String> values) throws CommandParamException {
        try {
            return values.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Failed to parse int parameter list: invalid number format: " + values);
        }
    }

    /**
     * Parses long value; throws {@link CommandParamException} if value cannot be parsed
     * @param value value to be parsed
     * @return parsed long value
     * @throws CommandParamException if value cannot be parsed
     */
    public static long parseLongParam(@NotNull String value) throws CommandParamException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }

    /**
     * Parses {@link User.Status} value; throws {@link CommandParamException} if value cannot be parsed
     * @param value value to be parsed
     * @return parsed value
     * @throws CommandParamException if value cannot be parsed
     */
    public static @NotNull User.Status parseUserStatusParam(@NotNull String value) throws CommandParamException {
        try {
            return User.Status.valueOf(value);
        } catch(IllegalArgumentException ex) {
            throw new CommandParamException("User.Status cannot be parsed: invalid status: " + value);
        }
    }

    /**
     * Parses {@link User.Role} value; throws {@link CommandParamException} if value cannot be parsed
     * @param value value to be parsed
     * @return parsed value
     * @throws CommandParamException if value cannot be parsed
     */
    public static @NotNull User.Role parseUserRoleParam(@NotNull String value) throws CommandParamException {
        try {
            return User.Role.valueOf(value);
        } catch(IllegalArgumentException ex) {
            throw new CommandParamException("User.Role cannot be parsed: invalid role: " + value);
        }
    }

    /**
     * Parses {@link Tariff.Status} value; throws {@link CommandParamException} if value cannot be parsed
     * @param value value to be parsed
     * @return parsed value
     * @throws CommandParamException if value cannot be parsed
     */
    public static @NotNull Tariff.Status parseTariffStatusParam(@NotNull String value) throws CommandParamException {
        try {
            return Tariff.Status.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new CommandParamException("Tariff.Status cannot be parsed: invalid status: " + value);
        }
    }
}
