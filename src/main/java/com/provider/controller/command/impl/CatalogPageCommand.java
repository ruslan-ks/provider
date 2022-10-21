package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.CatalogParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.service.TariffService;
import com.provider.sorting.TariffOrderByField;
import com.provider.sorting.TariffOrderRule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CatalogPageCommand extends FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(CatalogPageCommand.class);

    /**
     * Key: enum value used to obtain sorted tariffs
     * Value: title that will be shown to user
     */
    private static final Map<String, String> TARIFF_ORDER_BY_FIELD_STRING_MAP = Map.of(
            TariffOrderByField.TITLE.name(), "Title",
            TariffOrderByField.USD_PRICE.name(), "Price"
    );

    private static final String DEFAULT_TARIFF_ORDER_BY_FIELD = TariffOrderByField.TITLE.name();

    public CatalogPageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws DBException, ServletException, IOException, CommandParamException {
        final String tariffOrderByFieldParam = getParam(CatalogParams.ORDER_BY_FIELD)
                .orElse(DEFAULT_TARIFF_ORDER_BY_FIELD);
        final TariffOrderByField tariffOrderByField = parseTariffOrderByField(tariffOrderByFieldParam);
        final boolean isOrderDesc = getParam(CatalogParams.IS_ORDER_DESC).isPresent();
        final TariffOrderRule tariffOrderRule = TariffOrderRule.of(tariffOrderByField, isOrderDesc);

        final TariffService tariffService = serviceFactory.getTariffService();

        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final String locale = getLocale();
        final List<TariffDto> tariffDtoList = tariffService.findTariffsPage(offset, pageSize, locale, true, tariffOrderRule);
        request.setAttribute(RequestAttributes.TARIFFS, tariffDtoList);

        paginationHelper.setPageCountAttribute(tariffService.countActiveTariffs());

        request.setAttribute(RequestAttributes.TARIFF_ORDER_BY_FIELDS, TARIFF_ORDER_BY_FIELD_STRING_MAP);

        return newCommandResult(Paths.CATALOG_JSP);
    }

    private static TariffOrderByField parseTariffOrderByField(@NotNull String value) throws CommandParamException {
        try {
            return TariffOrderByField.valueOf(value);
        } catch (IllegalArgumentException ex) {
            logger.warn("TariffOrderByField cannot be parsed: illegal value: {}", value);
            throw new CommandParamException(ex);
        }
    }
}
