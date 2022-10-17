package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Tariff dao
 */
public abstract class TariffDao extends EntityDao<Integer, Tariff> {

    /**
     * Defines tariff records sorting order
     */
    public static class OrderRule {

        /**
         * Defines tariff records sorting by different fields
         */
        public enum OrderByField {
            ID,
            TITLE,
            STATUS,
            USD_PRICE
        }

        /**
         * Sort by id ascending
         */
        public static final OrderRule BY_ID = of(OrderByField.ID, false);

        /**
         * Sort by title ascending
         */
        public static final OrderRule BY_TITLE = of(OrderByField.TITLE, false);

        /**
         * Sort by title descending
         */
        public static final OrderRule BY_TITLE_DESC = of(OrderByField.TITLE, true);

        /**
         * Sort by price ascending
         */
        public static final OrderRule BY_PRICE = of(OrderByField.USD_PRICE, false);

        private final OrderByField orderByField;
        private final boolean desc;

        private OrderRule(@NotNull OrderByField orderByField, boolean desc) {
            this.orderByField = orderByField;
            this.desc = desc;
        }

        public static OrderRule of(@NotNull OrderByField orderByField, boolean desc) {
            return new OrderRule(orderByField, desc);
        }

        public OrderByField getOrderByField() {
            return orderByField;
        }

        public boolean isDesc() {
            return desc;
        }
    }

    /**
     * Returns Optional containing TariffDto if one is present, empty Optional otherwise.<br>
     * If there is no translation for the specified {@code locale}, the default one will be returned.
     * @param id tariff id
     * @param locale desired locale
     * @return Optional containing TariffDto if one is present, empty Optional otherwise
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull Optional<TariffDto> findFullInfoByKey(int id, @NotNull String locale)
            throws DBException;

    /**
     * Returns DTO containing Tariff, TariffDuration and {@code List<Service>}.
     * If there is no translation for the specified {@code locale}, the default one will be returned.
     * @param offset offset
     * @param limit limit
     * @param locale desired locale
     * @return List of TariffDto
     * @throws DBException if SQLException occurred
     */
    public abstract @NotNull List<TariffDto> findFullInfoPage(long offset, int limit, @NotNull String locale)
            throws DBException;

    /**
     * Adds tariff service link
     * @param tariffId tariff id
     * @param serviceIds service ids to be added to the tariff
     * @return true if db changes were made successfully
     * @throws DBException if SQLException occurred
     * @throws IllegalArgumentException if {@code tariffId <= 0}
     */
    public abstract boolean addServices(int tariffId, @NotNull Set<Integer> serviceIds) throws DBException;

    /**
     * Returns list of tariff services
     * @param tariffId tariff id
     * @return {@code List<Service>} belonging to the tariff with tariffId
     * @throws DBException if {@link ServiceDao} throws DBException
     * @throws IllegalArgumentException if tariffId <= 0
     */
    public abstract List<Service> findTariffServices(int tariffId, @NotNull String locale) throws DBException;

    /**
     * Returns records count
     * @return all tariffs count
     * @throws DBException if SQLException occurres
     */
    public abstract int countAll() throws DBException;
}
