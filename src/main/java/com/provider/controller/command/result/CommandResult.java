package com.provider.controller.command.result;

import com.provider.constants.Paths;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.util.ParameterizedUrl;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

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

    /**
     * There is no need to do either forward or redirect.<br>
     * This result object may be used when returning something other than HTML page
     */
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

    /**
     * Returns queue of messages of different types
     * @return queue of messages of different types
     */
    @NotNull Queue<Pair<MessageType, String>> getMessages();

    // TODO: move it somewhere
    static @NotNull CommandResult editTariffPage(int tariffId, @NotNull String editLocale) {
        final ParameterizedUrl url = ParameterizedUrl.of(Paths.EDIT_TARIFF_PAGE);
        url.addParam(TariffParams.ID, String.valueOf(tariffId));
        url.addParam(EditParams.LOCALE, editLocale);
        return CommandResultImpl.of(url.getString());
    }
}
