package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.CatalogParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Subscription;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.AccountService;
import com.provider.service.SubscriptionService;
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
import java.util.*;
import java.util.stream.Collectors;

public class CatalogPageCommand extends FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(CatalogPageCommand.class);

    /**
     * Key: enum value used to obtain sorted tariffs<br>
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
        // Order rule
        final String tariffOrderByFieldParam = getParam(CatalogParams.ORDER_BY_FIELD)
                .orElse(DEFAULT_TARIFF_ORDER_BY_FIELD);
        final TariffOrderByField tariffOrderByField = parseTariffOrderByField(tariffOrderByFieldParam);
        final boolean isOrderDesc = getParam(CatalogParams.IS_ORDER_DESC).isPresent();
        final TariffOrderRule tariffOrderRule = TariffOrderRule.of(tariffOrderByField, isOrderDesc);

        // Filter rules
        final Set<String> serviceIdFilterParams = getParamValues(CatalogParams.SERVICE_ID_FILTER);
        final Set<Integer> serviceIdFilters = CommandUtil.parseIntParams(serviceIdFilterParams);

        final TariffService tariffService = serviceFactory.getTariffService();

        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final String locale = getLocale();
        final List<TariffDto> tariffDtoList = tariffService.findTariffsPage(offset, pageSize, locale,
                Set.of(tariffOrderRule), serviceIdFilters, true);
        request.setAttribute(RequestAttributes.TARIFFS, tariffDtoList);

        // Set service tariffs count
        final Map<Service, Integer> serviceTariffCount = tariffService.findAllServicesTariffsCount(locale, true);
        request.setAttribute(RequestAttributes.SERVICE_COUNT_MAP, serviceTariffCount);

        final int recordsCount = serviceTariffCount.entrySet().stream()
                        .filter(e -> serviceIdFilters.contains(e.getKey().getId()))
                        .map(Map.Entry::getValue)
                        .reduce(Integer::sum)
                        .orElse(tariffService.countActiveTariffs());
        paginationHelper.computeAndSetPageCountAttribute(recordsCount);
        logger.trace("Records count: " + recordsCount);

        // Adding user active subscription tariff ids to request scope
        final Optional<User> user = getSessionUser();
        if (user.isPresent()) {
            final AccountService accountService = serviceFactory.getAccountService();
            final UserAccount userAccount = accountService.findUserAccount(user.get())
                    .orElseThrow(() -> new RuntimeException("User account not found! User: " + user.get()));
            final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
            final List<Subscription> activeSubscriptions = subscriptionService.findActiveSubscriptions(userAccount);
            final Set<Integer> activeSubscriptionTariffIds = activeSubscriptions.stream()
                    .map(Subscription::getTariffId)
                    .collect(Collectors.toSet());
            request.setAttribute(RequestAttributes.USER_SUBSCRIBED_TARIFF_IDS, activeSubscriptionTariffIds);
        }

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
