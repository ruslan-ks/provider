package com.provider.controller.command.impl;

import com.provider.constants.AppInitParams;
import com.provider.constants.Messages;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResults;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class EditTariffCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(EditTariffCommand.class);

    public EditTariffCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final Map<String, String> params = getRequiredParams(TariffParams.ID, TariffParams.TITLE,
                TariffParams.DESCRIPTION, TariffParams.STATUS, EditParams.LOCALE);

        final int tariffId = CommandUtil.parseIntParam(params.get(TariffParams.ID));
        final String editLocale = params.get(EditParams.LOCALE);

        final TariffService tariffService = serviceFactory.getTariffService();
        final Tariff tariff = tariffService.findTariffById(tariffId)
                .orElseThrow(() -> new CommandParamException("Tariff not found! tariff id: " + tariffId));

        // Resulting page
        final CommandResult commandResult = CommandResults.editTariffPage(tariffId, editLocale);

        // Setting new values
        final Tariff.Status tariffStatus = CommandUtil.parseTariffStatusParam(params.get(TariffParams.STATUS));
        setUpdatableTariffFields(tariff, params.get(TariffParams.TITLE), params.get(TariffParams.DESCRIPTION),
                tariffStatus);

        return tryUpdateOrUpsert(tariff, tariffService, editLocale, commandResult);
    }

    private void setUpdatableTariffFields(@NotNull Tariff tariff, @NotNull String title, @NotNull String description,
                                          @NotNull Tariff.Status status) {
        tariff.setTitle(title);
        tariff.setDescription(description);
        tariff.setStatus(status);
    }

    private @NotNull CommandResult tryUpdateOrUpsert(@NotNull Tariff tariff, @NotNull TariffService tariffService,
                                                     @NotNull String editLocale, @NotNull CommandResult commandResult)
            throws DBException {
        final String defaultLocale = request.getServletContext().getInitParameter(AppInitParams.DEFAULT_LOCALE);
        boolean updated = false;
        try {
            updated = editLocale.equals(defaultLocale)
                    ? tariffService.updateTariff(tariff)
                    : tariffService.upsertTariffTranslation(tariff, editLocale);
        } catch (ValidationException ex) {
            logger.warn("Failed to update tariff! Invalid tariff data!", ex);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_TARIFF_PARAMS);
        }
        if (updated) {
            logger.info("Updated tariff: {}", tariff);
            return commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.TARIFF_EDIT_SUCCESS);
        }
        logger.warn("Failed to update tariff: {}", tariff);
        return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_EDIT_FAIL);
    }
}
