package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Utility class
 */
public class CommandUtil {
    private CommandUtil() {}

    /**
     * Parse long parameter
     * @param value value to be parsed
     * @return long extracted from 'value' argument
     * @throws CommandParamException if NumberFormatException occurred
     */
    public static long parseLongParam(@NotNull String value) throws CommandParamException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: " + value);
        }
    }

    /**
     * Parse BigDecimal parameter
     * @param value value to be parsed
     * @return extracted BigDecimal valud
     * @throws CommandParamException if NumberFormatException occurred
     */
    public static BigDecimal parseBigDecimalParam(@NotNull String value) throws CommandParamException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new CommandParamException("Invalid number format: value = " + value);
        }
    }
}
