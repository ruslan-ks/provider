package com.provider.dao;

import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;

import java.math.BigDecimal;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Test data class. Provides data for parameterized tests
 */
public class DaoTestData {
    private DaoTestData() {}

    private static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    /**
     * Returns list of services for test cases
     * @return list of services
     */
    public static Stream<Service> getServiceStream() {
        return Stream.of("Internet", "TV", "Telephone", "Mobile network", "Satellite TV")
                .map(s -> entityFactory.newService(0, s, s + " is the best choice!"));
    }

    /**
     * Returns stream of test users
     * @return stream of test user objects
     */
    public static Stream<User> getUserStream() {
        return IntStream.iterate(0, i -> ++i)
                .limit(8)
                .mapToObj(DaoTestData::getTestUser);
    }

    /**
     * Returns stream of test Tariff objects
     * @return stream of test Tariff objects
     */
    public static Stream<Tariff> getTariffStream() {
        return Stream.of(
                entityFactory.newTariff(0, "Meganet", "Meganet description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(100), "no image"),
                entityFactory.newTariff(0, "Supernet", "Supernet description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(50), "no image"),
                entityFactory.newTariff(0, "Cool tariff", "Cool tariff description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(200), "no image"),
                entityFactory.newTariff(0, "Mobile pack", "Mobile pack description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(100), "no image"),
                entityFactory.newTariff(0, "Magic tariff", "Magic tariff description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(200), "no image")
        );
    }

    private static User getTestUser(int i) {
        return UserImpl.of(i, "user" + i, "sur" + i, "name" + i, "login" + i,
                User.Role.values()[i % User.Role.values().length], User.Status.ACTIVE);
    }
}
