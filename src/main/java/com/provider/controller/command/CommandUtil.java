package com.provider.controller.command;

import com.provider.controller.command.exception.CommandParamException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class
 */
public class CommandUtil {
    private CommandUtil() {}


    /**
     * Throws CommandParamException if any of objects is null
     * @param objects objects to be checked
     * @throws CommandParamException if any of arguments is null
     */
    public static void throwIfNullParam(String... objects) throws CommandParamException {
        if (Arrays.stream(objects).anyMatch(Objects::isNull)) {
            throw new CommandParamException();
        }
    }

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
            throw new CommandParamException("Invalid number format: " + value);
        }
    }
}
