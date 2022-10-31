package com.provider.controller.command.result;

import com.provider.constants.Paths;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.util.ParameterizedUrl;
import org.jetbrains.annotations.NotNull;

/**
 * Command execution result
 * Provides an ability for controller servlet to return command-chosen view
 */
public interface CommandResult {

    /**
     * Message type enum. Used when obtaining and adding messages of different types
     */
    enum MessageType {
        FAIL,
        SUCCESS
    }

    CommandResult NO_VIEW = CommandResultImpl.of("");

    /**
     * Returns resulting page location
     */
    @NotNull String getViewLocation();

    /**
     * Adds message of a specified type
     * @param messageType message type
     * @param message message
     * @return reference to the same object
     */
    @NotNull CommandResult addMessage(@NotNull MessageType messageType, @NotNull String message);

    static @NotNull CommandResult editTariffPage(int tariffId, @NotNull String editLocale) {
        final ParameterizedUrl url = ParameterizedUrl.of(Paths.EDIT_TARIFF_PAGE);
        url.addParam(TariffParams.ID, String.valueOf(tariffId));
        url.addParam(EditParams.LOCALE, editLocale);
        return CommandResultImpl.of(url.getString());
    }
}
