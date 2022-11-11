package com.provider.controller.command.result;

import com.provider.constants.Paths;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.util.ParameterizedUrl;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class that generates different command results
 */
public class CommandResults {
    private CommandResults() {}

    /**
     * Creates command result of edit tariff page
     * @param tariffId tariff id
     * @param editLocale locale
     * @return command result of edit tariff page with all the required parameters
     */
    public static @NotNull CommandResult editTariffPage(int tariffId, @NotNull String editLocale) {
        final ParameterizedUrl url = ParameterizedUrl.of(Paths.EDIT_TARIFF_PAGE);
        url.addParam(TariffParams.ID, String.valueOf(tariffId));
        url.addParam(EditParams.LOCALE, editLocale);
        return CommandResultImpl.of(url.getString());
    }
}
