package com.provider;

import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.entity.dto.SimpleTariffDto;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Service;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import com.provider.entity.product.impl.TariffDurationImpl;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Test data class. Provides data for parameterized tests
 */
public class TestData {
    private TestData() {}

    private static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    /**
     * Returns stream of services for test cases
     * @return stream of services
     */
    public static Stream<Service> serviceStream() {
        return Stream.of("Internet", "TV", "Telephone", "Mobile network", "Satellite TV")
                .map(s -> entityFactory.newService(0, s, s + " is the best choice!"));
    }

    /**
     * Returns list of services for test cases
     * @param n services count
     * @return list of services
     */
    public static Stream<Service> serviceStream(int n) {
        return Stream.iterate(0, i -> ++i)
                .limit(n)
                .map(i -> entityFactory.newService(0, "Service " + i, "Service " + i + " description"));
    }

    /**
     * Returns stream of arguments of service default name, translated name and language
     * @return stream of arguments of service default name, translated name and language
     */
    public static Stream<Arguments> serviceNamesUkTranslations() {
        final String lang = "uk";
        return Stream.of(
                Arguments.of("TV", "Телебачення", lang),
                Arguments.of("Internet", "Інтернет", lang),
                Arguments.of("Mobile network", "Мобільна мережа", lang)
        );
    }

    /**
     * Returns stream of test users
     * @return stream of test user objects
     */
    public static Stream<User> userStream() {
        return IntStream.iterate(1, i -> ++i)
                .limit(10)
                .mapToObj(TestData::user);
    }

    /**
     * Returns stream of test users
     * @return stream of test user objects
     */
    public static Stream<User> zeroIdUserStream() {
        return IntStream.iterate(0, i -> ++i)
                .limit(10)
                .mapToObj(TestData::user)
                .map(u -> UserImpl.of(0, u.getName(), u.getSurname(), u.getLogin(), u.getPhone(), u.getRole(),
                        u.getStatus()));
    }

    private static User user(int i) {
        return UserImpl.of(i, "Name" + i, "Surname" + i, "user" + i,
                String.valueOf(i).repeat(8).substring(0, 8), User.Role.MEMBER, User.Status.ACTIVE);
    }

    /**
     * Returns stream of test Tariff objects
     * @return stream of test Tariff objects
     */
    public static Stream<Tariff> tariffStream() {
        return Stream.of(
                entityFactory.newTariff(0, "Meganet", "Meganet description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(100), "no_image"),
                entityFactory.newTariff(0, "Supernet", "Supernet description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(50), "no_image"),
                entityFactory.newTariff(0, "Cool tariff", "Cool tariff description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(200), "no_image"),
                entityFactory.newTariff(0, "Mobile pack", "Mobile pack description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(100), "no_image"),
                entityFactory.newTariff(0, "Magic tariff", "Magic tariff description",
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(200), "no_image")
        );
    }

    public static Stream<TariffDto> tariffDtoStream() {
        return tariffStream()
                .map(TestData::tariffDtoOf);
    }

    private static TariffDto tariffDtoOf(Tariff tariff) {
        final TariffDuration tariffDuration = TariffDurationImpl.of(tariff.getId(), 1, 60);
        final List<Service> services = serviceStream().toList();
        return SimpleTariffDto.of(tariff, tariffDuration, services);
    }

    /**
     * Returns stream of test Tariff objects
     * @param n tariffs count
     * @return stream of test Tariff objects
     */
    public static Stream<Tariff> tariffStream(int n) {
        return Stream.iterate(0, i -> ++i)
                .limit(n)
                .map(i -> entityFactory.newTariff(0, "Tariff " + i, "Description " + i,
                        Tariff.Status.ACTIVE, BigDecimal.valueOf(i * 10), "no_image"));
    }
}
