package com.provider.service;

import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.UserAccount;
import org.jetbrains.annotations.NotNull;

public interface SubscriptionService {
    /**
     * Withdraws funds from {@code userAccount} and adds subscription for {@code tariff} <br>
     * @param userAccount user account to be used for payment
     * @param tariff tariff to be subscribed for
     * @return true if subscription was successfully added and money was withdrawn from the supplied account<br>
     * false if subscription already exists and is active
     */
    boolean buySubscription(@NotNull UserAccount userAccount, @NotNull Tariff tariff) throws DBException;

    /**
     * Returns true if account money amount is enough to pay for tariff once
     * @param userAccount user account
     * @param tariff tariff
     * @return true if account money amount is enough to pay for tariff once, false otherwise
     */
    boolean hasEnoughMoneyToPay(@NotNull UserAccount userAccount, @NotNull Tariff tariff);

    /**
     * Checks whether subscription exists and is active
     * @param userAccount user account bound to the subscription
     * @param tariff tariff of the subscription
     * @return true if subscription of {@code userAccount} and {@code tariff} exists and has {@code ACTIVE} status
     */
    boolean activeSubscriptionExists(@NotNull UserAccount userAccount, @NotNull Tariff tariff) throws DBException;
}
