package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EditTariffPageCommand extends AdminCommand {
    public EditTariffPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final int tariffId = CommandUtil.parseIntParam(getRequiredParam(TariffParams.ID));
        final String locale = getRequiredParam(EditParams.LOCALE);

        final TariffService tariffService = serviceFactory.getTariffService();
        final TariffDto tariffDto = tariffService.findTariffFullInfoById(tariffId, locale)
                .orElseThrow(() -> new CommandParamException("Tariff not found! Tariff id: " + tariffId));

        request.setAttribute(RequestAttributes.TARIFF_DTO, tariffDto);

        return newCommandResult(Paths.EDIT_TARIFF_JSP);
    }
}
