package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TariffsManagementPageCommand extends AdminCommand {
    TariffsManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException, CommandParamException {
        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final String locale = getLocale();
        final TariffService tariffService = serviceFactory.getTariffService();

        final List<TariffDto> tariffsList = tariffService.findTariffsPage(offset, pageSize, locale, false);
        request.setAttribute(RequestAttributes.TARIFFS, tariffsList);

        final int tariffsCount = tariffService.countAllTariffs();
        paginationHelper.setPageCountAttribute(tariffsCount);

        final List<Service> serviceList = tariffService.findAllServices(locale);
        request.setAttribute(RequestAttributes.SERVICES, serviceList);

        return CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_JSP);
    }
}
