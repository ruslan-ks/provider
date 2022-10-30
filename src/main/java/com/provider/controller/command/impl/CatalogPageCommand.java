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
        final TariffOrderRule tariffOrderRule = getTariffOrderRule();
        final Set<Integer> serviceIdFilters = getServiceIdFilters();

        final TariffService tariffService = serviceFactory.getTariffService();

        final PaginationHelper paginationHelper = getPaginationHelper();
        final int pageSize = paginationHelper.getPageSize();
        final long offset = paginationHelper.getOffset();

        final String locale = getLocale();
        final List<TariffDto> tariffDtoList = tariffService.findTariffsPage(offset, pageSize, locale,
                Set.of(tariffOrderRule), serviceIdFilters, true);
        request.setAttribute(RequestAttributes.TARIFFS, tariffDtoList);

        // Set service tariffs count map
        final Map<Service, Integer> serviceTariffCountMap = tariffService.findAllServicesTariffsCount(locale, true);
        request.setAttribute(RequestAttributes.SERVICE_COUNT_MAP, serviceTariffCountMap);

        final int recordsCount = computeTariffsCount(serviceIdFilters, tariffService);
        paginationHelper.computeAndSetPageCountAttribute(recordsCount);
        logger.trace("Records count: " + recordsCount);

        // Adding user active subscription tariff ids to request scope
        final Optional<User> user = getSessionUser();
        if (user.isPresent()) {
            final Set<Integer> activeSubscriptionTariffIds = getUserActiveSubscriptionTariffIds(user.get());
            request.setAttribute(RequestAttributes.USER_SUBSCRIBED_TARIFF_IDS, activeSubscriptionTariffIds);
        }

        request.setAttribute(RequestAttributes.TARIFF_ORDER_BY_FIELDS, TARIFF_ORDER_BY_FIELD_STRING_MAP);

        return newCommandResult(Paths.CATALOG_JSP);
    }

    private @NotNull TariffOrderRule getTariffOrderRule() throws CommandParamException {
        final String tariffOrderByFieldParam = getParam(CatalogParams.ORDER_BY_FIELD)
                .orElse(DEFAULT_TARIFF_ORDER_BY_FIELD);
        final TariffOrderByField tariffOrderByField = parseTariffOrderByField(tariffOrderByFieldParam);
        final boolean isOrderDesc = getParam(CatalogParams.IS_ORDER_DESC).isPresent();
        return TariffOrderRule.of(tariffOrderByField, isOrderDesc);
    }

    private @NotNull Set<Integer> getServiceIdFilters() throws CommandParamException {
        return CommandUtil.parseIntParams(getParamValues(CatalogParams.SERVICE_ID_FILTER));
    }

    private int computeTariffsCount(@NotNull Set<Integer> chosenServiceIdFilters, @NotNull TariffService tariffService) throws DBException {
        return chosenServiceIdFilters.isEmpty()
                ? tariffService.countActiveTariffs()
                : tariffService.countDistinctTariffsIncludingServices(chosenServiceIdFilters, true);
    }

    private @NotNull Set<Integer> getUserActiveSubscriptionTariffIds(@NotNull User user) throws DBException {
        final AccountService accountService = serviceFactory.getAccountService();
        final UserAccount userAccount = accountService.findUserAccount(user);
        final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
        final List<Subscription> activeSubscriptions = subscriptionService.findActiveSubscriptions(userAccount);
        return activeSubscriptions.stream()
                .map(Subscription::getTariffId)
                .collect(Collectors.toSet());
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
