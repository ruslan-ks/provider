package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.ServiceParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
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

public class AddServiceCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddServiceCommand.class);

    AddServiceCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final Map<String, String> params = getRequiredParams(ServiceParams.NAME, ServiceParams.DESCRIPTION);
        final Service service = entityFactory.newService(0, params.get(ServiceParams.NAME),
                params.get(ServiceParams.DESCRIPTION));
        final TariffService tariffService = serviceFactory.getTariffService();
        final CommandResult commandResult = newCommandResult(Paths.TARIFFS_MANAGEMENT_PAGE);
        try {
            final boolean inserted = tariffService.insertService(service);
            if (inserted) {
                commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.SERVICE_INSERT_SUCCESS);
            } else {
                commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.SERVICE_INSERT_FAIL);
            }
        } catch (ValidationException ex) {
            logger.warn("Failed to insert service: invalid service {}", service);
            logger.warn("Failed to insert service", ex);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.SERVICE_INSERT_FAIL);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_SERVICE_PARAMS);
        }
        return commandResult;
    }
}
