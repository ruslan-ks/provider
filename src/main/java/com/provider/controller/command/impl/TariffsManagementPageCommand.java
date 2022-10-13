package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TariffsManagementPageCommand extends AdminCommand {
    public TariffsManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected CommandResult executeAccessed(@NotNull User user)
            throws DBException {
        final TariffService tariffService = serviceFactory.getTariffService();
        final String locale = getUserSettingsLocale().orElseThrow();
        final List<Service> serviceList = tariffService.findAllServices(locale);
        request.setAttribute(RequestAttributes.SERVICES, serviceList);
        return CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_JSP);
    }
}
