package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.PaginationParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
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
    private static final int PAGE_SIZE = 5;

    TariffsManagementPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user) throws DBException, CommandParamException {
        final String pageNumberParam = getParam(PaginationParams.PAGE_NUMBER).orElse("1");
        final int pageNumber = CommandUtil.parseIntParam(pageNumberParam);
        if (pageNumber < 0) {
            throw new CommandParamException("Invalid parameter:" + PaginationParams.PAGE_NUMBER + "(" + pageNumber +
                    ") < 0");
        }
        final String pageSizeParam = getParam(PaginationParams.PAGE_SIZE).orElse(String.valueOf(PAGE_SIZE));
        final int pageSize = CommandUtil.parseIntParam(pageSizeParam);
        if (pageSize < 1) {
            throw new CommandParamException("Invalid parameter:" + PaginationParams.PAGE_SIZE + "(" + pageSize +
                    ") < 1");
        }
        final long offset = (long) (pageNumber - 1) * pageSize;

        final String locale = getUserSettingsLocale().orElseThrow();
        final TariffService tariffService = serviceFactory.getTariffService();

        final List<TariffDto> tariffsList = tariffService.findTariffsPage(offset, pageSize, locale);
        request.setAttribute(RequestAttributes.TARIFFS, tariffsList);

        final List<Service> serviceList = tariffService.findAllServices(locale);
        request.setAttribute(RequestAttributes.SERVICES, serviceList);

        final int pageCount = (int) Math.ceil((double) tariffService.countAllTariffs() / pageSize);
        request.setAttribute(RequestAttributes.PAGE_COUNT, pageCount);

        return CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_JSP);
    }
}
