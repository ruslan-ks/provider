package com.provider.functions;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Subscription;
import com.provider.entity.product.TariffDuration;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import com.provider.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

public class TimeFunctions {
    private TimeFunctions() {}

    private static final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();

    private static final Logger logger = LoggerFactory.getLogger(TimeFunctions.class);

    public static Date nextPaymentTime(Subscription subscription, TariffDuration tariffDuration) {
        try {
            final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
            final var date = Date.from(subscriptionService.computeNextPaymentTime(subscription, tariffDuration));
            logger.trace("Date from Instant: {}", date);
            return date;
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }
}
