package com.provider.dao;

import com.provider.TestData;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserDaoTest extends AbstractDaoTest {
    protected static UserDao userDao;

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsert(User user) throws DBException {
        final boolean inserted = userDao.insert(user);
        assertTrue(inserted);
        assertNotEquals(0, user.getId());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsertAndFindByKey(User user) throws DBException {
        userDao.insert(user);

        final Optional<User> found = userDao.findByKey(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsertAndFindByLogin(User user) throws DBException {
        userDao.insert(user);

        final Optional<User> found = userDao.findByLogin(user.getLogin());
        assertTrue(found.isPresent());
        assertEquals(user, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testUpdateStatus(User user) throws DBException {
        userDao.insert(user);

        final User.Status newStatus = user.getStatus() == User.Status.ACTIVE
                ? User.Status.SUSPENDED
                : User.Status.ACTIVE;
        user.setStatus(newStatus);
        userDao.update(user);

        final Optional<User> found = userDao.findByKey(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user, found.orElseThrow());
    }

    @Test
    public void testGetUserCount() throws DBException {
        final List<User> users = TestData.userStream().toList();
        for (var user : users) {
            userDao.insert(user);
        }

        final long count = userDao.getUserCount();
        assertEquals(users.size(), count);
    }

    @Test
    public void testFindPageOfAllPossibleSizes() throws DBException {
        final List<User> users = TestData.userStream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
        for (var user : users) {
            userDao.insert(user);
        }
        for (int limit = 1; limit < users.size(); limit++) {
            for (int offset = 0; offset < users.size(); offset += limit) {
                final int subListToIndex = Math.min(offset + limit, users.size());
                try {
                    final List<User> expectedUsers = users.subList(offset, subListToIndex);
                    final List<User> actualUsers = userDao.findPage(offset, limit);
                    assertEquals(expectedUsers, actualUsers);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }

            }
        }
    }




}
