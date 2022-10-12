package com.provider.entity;

import com.provider.entity.product.*;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Entity abstract factory
 * Creates entities of a model
 */
public interface EntityFactory {
    /**
     * Creates new User instance
     * @param id user id
     * @param name user name
     * @param surname user surname
     * @param login user login
     * @param phone user phone
     * @param role user role
     * @param status user status
     * @return new User instance with the specified parameters
     */
    @NotNull User newUser(long id, @NotNull String name, @NotNull String surname, @NotNull String login,
                          @NotNull String phone, @NotNull User.Role role, @NotNull User.Status status);

    /**
     * Creates new UserAccount instance
     * @param id account id
     * @param userId user id
     * @param currency account currency
     * @return new UserAccount instance with the specified parameters
     */
    @NotNull UserAccount newUserAccount(long id, long userId, @NotNull Currency currency);

    /**
     * Creates new UserAccount
     * @param id account id
     * @param userId user id
     * @param currency account currency
     * @param amount money amount
     * @return new UserAccount instance with the specified parameters
     */
    @NotNull UserAccount newUserAccount(long id, long userId, @NotNull Currency currency, @NotNull BigDecimal amount);

    /**
     * Creates UserPassword instance
     * @param userId user id
     * @param hash hashed password
     * @param salt salt used when hashing the password
     * @param hashMethod mash method
     * @return new UserPassword instance with the specified parameters
     */
    @NotNull UserPassword newUserPassword(long userId, @NotNull String hash, @NotNull String salt,
                                          @NotNull PasswordHashing.HashMethod hashMethod);

    /**
     * Creates new UserPassword
     * @param hash hashed password
     * @param salt salt used when hashing the password
     * @param hashMethod mash method
     * @return new UserPassword instance with the specified parameters
     */
    @NotNull UserPassword newUserPassword(@NotNull String hash, @NotNull String salt,
                                          @NotNull PasswordHashing.HashMethod hashMethod);

    /**
     * Creates new Service instance
     * @param id service id
     * @param name service name
     * @return new Service instance
     */
    @NotNull Service newService(int id, @NotNull String name, @NotNull String description);

    /**
     * Creates new TariffDuration instance
     * @param tariffId tariff id
     * @param months months count
     * @param minutes minutes count
     * @return new TariffDuration instance
     */
    @NotNull TariffDuration newTariffDuration(int tariffId, int months, long minutes);

    /**
     * Creates new Tariff instance
     * @param id tariff id
     * @param title tariff title
     * @param status tariff status
     * @param usdPrice tariff usd price
     * @return new Tariff instance
     */
    @NotNull Tariff newTariff(int id, @NotNull String title, @NotNull Tariff.Status status,
                              @NotNull BigDecimal usdPrice);

    // TODO: delete if still unused
    /**
     * Creates new TariffService instance
     * @param tariffId tariff id
     * @param serviceId service id
     * @return new TariffService instance
     */
    @NotNull TariffService newTariffService(int tariffId, int serviceId);
}
