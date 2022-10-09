package com.provider.dao;

import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.entity.product.Service;
import com.provider.entity.user.User;
import com.provider.entity.user.impl.UserImpl;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DaoTestData {
    private DaoTestData() {}

    private static final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    /**
     * Returns list of services for test cases
     * @return list of services
     */
    public static Stream<Service> getServiceStream() {
        return Stream.of("Internet", "TV", "Telephone", "Mobile network", "Radio", "Satellite TV")
                .map(s -> entityFactory.newService(0, s));
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

    private static User getTestUser(int i) {
        return UserImpl.of(i, "user" + i, "sur" + i, "name" + i, "login" + i,
                User.Role.values()[i % User.Role.values().length], User.Status.ACTIVE);
    }
}
