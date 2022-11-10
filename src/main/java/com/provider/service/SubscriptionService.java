package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.SubscriptionDto;
import com.provider.entity.dto.SubscriptionTariffDto;
import com.provider.entity.product.Subscription;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public abstract class SubscriptionService extends AbstractService {
    protected SubscriptionService() throws DBException {}

    /**
     * Withdraws funds from {@code userAccount} and adds subscription for {@code tariff} <br>
     * @param userAccount user account to be used for payment
     * @param tariff tariff to be subscribed for
     * @return true if subscription was successfully added and money was withdrawn from the supplied account<br>
     * false if subscription already exists and is active
     */
    public abstract boolean buySubscription(@NotNull UserAccount userAccount, @NotNull Tariff tariff) throws DBException;

    /**
     * Returns true if account money amount is enough to pay for tariff once
     * @param userAccount user account
     * @param tariff tariff
     * @return true if account money amount is enough to pay for tariff once, false otherwise
     */
    public abstract boolean hasEnoughMoneyToPay(@NotNull UserAccount userAccount, @NotNull Tariff tariff);

    /**
     * Checks whether subscription exists and is active
     * @param userAccount user account bound to the subscription
     * @param tariff tariff of the subscription
     * @return true if subscription of {@code userAccount} and {@code tariff} exists and has {@code ACTIVE} status
     */
    public abstract boolean activeSubscriptionExists(@NotNull UserAccount userAccount, @NotNull Tariff tariff)
            throws DBException;

    /**
     * Returns list of subscriptions bound to {@code userAccount} and with status == ACTIVE
     * @param userAccount user account
     * @return list of subscriptions bound to {@code userAccount} and with status == ACTIVE
     * @throws DBException if {@link com.provider.dao.SubscriptionDao} throws it
     */
    public abstract @NotNull List<Subscription> findActiveSubscriptions(@NotNull UserAccount userAccount)
            throws DBException;

    /**
     * Returns list of SubscriptionTariffDto containing Subscription and TariffDto objects
     * @param userAccount user account bound to subscriptions
     * @return list of SubscriptionTariffDto containing Subscription and TariffDto objects
     * @throws DBException if SLQException occurred
     */
    public abstract @NotNull List<SubscriptionTariffDto> findActiveSubscriptionsFullInfo(
            @NotNull UserAccount userAccount, @NotNull String locale) throws DBException;

    /**
     * Computes next payment time using subscription last payment time and tariff duration
     * @param subscription subscription
     * @param tariffDuration subscription tariff duration
     * @return instant when fee must be charged
     * @throws IllegalArgumentException if subscription's tariff id != tariffDuration's tariff id
     */
    public abstract @NotNull Instant computeNextPaymentTime(@NotNull Subscription subscription,
                                                            @NotNull TariffDuration tariffDuration);

    /**
     * Inactivates subscription
     * @param subscription subscription to be inactivated
     * @return true if db changes were made successfully
     */
    public abstract boolean unsubscribe(@NotNull Subscription subscription) throws DBException;

    /**
     * Reruns list of subscriptions that need to be renewed
     * @return list of subscriptions that need to be renewed. Active subscriptions only
     * @throws DBException if {@link com.provider.dao.SubscriptionDao} throws it
     */
    public abstract List<SubscriptionDto> findAllExpiredActiveSubscriptions() throws DBException;

    /**
     * Renews all expired active subscriptions
     * @param renewedConsumer is called when subscription is renewed successfully
     * @param notEnoughMoneyConsumer is called when subscription if failed to renew, cause user account
     *                               does not have enough money
     * @throws DBException if {@link com.provider.dao.SubscriptionDao} or {@link com.provider.dao.UserAccountDao}
     * throws it
     */
    public abstract void renewAllExpiredActiveSubscriptions(@NotNull Consumer<SubscriptionDto> renewedConsumer,
            @NotNull Consumer<SubscriptionDto> notEnoughMoneyConsumer) throws DBException;
}
