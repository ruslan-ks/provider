package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import com.provider.entity.user.UserPassword;
import com.provider.entity.user.hashing.PasswordHashing;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserPasswordDaoTest extends AbstractDaoTest {

    protected abstract UserPasswordDao getUserPasswordDao();

    protected abstract UserDao getUserDao();

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsert(User user) throws DBException {
        final UserDao userDao = getUserDao();
        userDao.setConnection(getConnection());
        userDao.insert(user);

        // password pretends to be hashed, but it's not - hashing is tested separately
        final UserPassword password = passwordFor(user);
        final UserPasswordDao userPasswordDao = getUserPasswordDao();
        userPasswordDao.setConnection(getConnection());
        final boolean inserted = userPasswordDao.insert(password);
        assertTrue(inserted);
        assertEquals(user.getId(), password.getUserId());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsertAndFindByKey(User user) throws DBException {
        final UserDao userDao = getUserDao();
        userDao.setConnection(getConnection());
        userDao.insert(user);

        // password pretends to be hashed, but it's not - hashing is tested separately
        final UserPassword password = passwordFor(user);
        final UserPasswordDao userPasswordDao = getUserPasswordDao();
        userPasswordDao.setConnection(getConnection());
        userPasswordDao.insert(password);

        final Optional<UserPassword> found = userPasswordDao.findByKey(password.getUserId());
        assertTrue(found.isPresent());
        assertEquals(password, found.orElseThrow());
    }

    private UserPassword passwordFor(User user) {
        return entityFactory.newUserPassword(user.getId(), "password" + user.getId(), "not a salt",
                PasswordHashing.HashMethod.PBKDF2_1);
    }
}
