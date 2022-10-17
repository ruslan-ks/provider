package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.provider.constants.params.TariffParams.*;

public class AddTariffCommand extends AdminCommand {
    public AddTariffCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAccessed(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final Map<String, String> params = getRequiredParams(TITLE, DESCRIPTION, USD_PRICE, STATUS, DURATION_MONTHS,
                DURATION_MINUTES);
        final List<String> tariffServiceIdParams = getRequiredParamValues(SERVICE_IDS);

        final String title = params.get(TITLE);
        final String description = params.get(DESCRIPTION);
        final Tariff.Status status = CommandUtil.parseTariffStatusParam(params.get(STATUS));
        final BigDecimal usdPrice = CommandUtil.parseBigDecimalParam(params.get(USD_PRICE));
        final int months = CommandUtil.parseIntParam(params.get(DURATION_MONTHS));
        final int minutes = CommandUtil.parseIntParam(params.get(DURATION_MINUTES));
        final Set<Integer> serviceIds = new HashSet<>(CommandUtil.parseIntParams(tariffServiceIdParams));

        final Tariff tariff = entityFactory.newTariff(0, title, description, status, usdPrice);
        final TariffDuration tariffDuration = entityFactory.newTariffDuration(0, months, minutes);

        final CommandResult commandResult = CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_PAGE);
        final TariffService tariffService = serviceFactory.getTariffService();
        try {
            final boolean inserted = tariffService.insertTariff(tariff, tariffDuration, serviceIds);
            if (inserted) {
                commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.TARIFF_INSERT_SUCCESS);
            } else {
                commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_INSERT_FAIL);
            }
        } catch (ValidationException ex) {
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_TARIFF_PARAMS);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_INSERT_FAIL);
        }
        return commandResult;
    }
}
