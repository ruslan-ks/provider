package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class CatalogPageCommand extends FrontCommand {
    public CatalogPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws DBException, ServletException, IOException, CommandParamException {
        // TODO: replace this method call with the one that will be returning string and throwing if locale is null
        final String locale = getUserSettingsLocale().orElseThrow();

        final TariffService tariffService = serviceFactory.getTariffService();

        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final List<TariffDto> tariffDtoList = tariffService.findTariffsPage(offset, pageSize, locale);
        request.setAttribute(RequestAttributes.TARIFFS, tariffDtoList);

        paginationHelper.setPageCountAttribute(tariffService.countAllTariffs());

        return newCommandResult(Paths.CATALOG_JSP);
    }
}
