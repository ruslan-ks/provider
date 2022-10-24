package com.provider.functions;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Subscription;
import com.provider.entity.product.TariffDuration;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import com.provider.service.SubscriptionService;

import java.time.Instant;
import java.util.Date;

public class TimeFunctions {
    private TimeFunctions() {}

    private static final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();

    public static Instant nextPaymentTime(Subscription subscription, TariffDuration tariffDuration) {
        try {
            final SubscriptionService subscriptionService = serviceFactory.getSubscriptionService();
            return subscriptionService.computeNextPaymentTime(subscription, tariffDuration);
        } catch (DBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }
}
