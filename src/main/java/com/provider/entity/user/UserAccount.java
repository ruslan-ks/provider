package com.provider.entity.user;

import com.provider.entity.Currency;
import com.provider.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Represents money account
 */
public interface UserAccount extends Entity {
    /**
     * Returns account id
     * @return account id
     */
    long getId();

    /**
     * Returns account owner id
     * @return owner id
     */
    long getUserId();

    /**
     * Sets account id
     * @param id account id
     * @throws IllegalArgumentException if param id <= 0
     */
    void setId(long id);

    /**
     * Returns account currency
     * @return account currency
     */
    @NotNull Currency getCurrency();

    /**
     * Returns money amount
     * @return money amount
     */
    @NotNull BigDecimal getAmount();

    /**
     * Replenish account
     * @param value positive value(> 0)
     * @throws IllegalArgumentException if value <= 0
     */
    void replenish(@NotNull BigDecimal value);

    /**
     * Withdraws funds
     * @param value money amount to be withdrawn
     * @throws IllegalArgumentException if {@code value < 0}
     * @throws IllegalStateException if {@code value} is greater than current account money amount
     */
    void withdraw(@NotNull BigDecimal value);
}
