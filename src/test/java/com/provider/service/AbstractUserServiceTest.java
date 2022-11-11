package com.provider.service;

import com.provider.dao.UserAccountDao;
import com.provider.dao.UserDao;
import com.provider.dao.UserPasswordDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.entity.user.UserPassword;
import com.provider.service.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    protected static UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserPasswordDao userPasswordDao;

    @Mock
    private UserAccountDao userAccountDao;

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testFindUserById(User user) throws DBException {
        when(userDao.findByKey(user.getId())).thenReturn(Optional.of(user));
        when(daoFactory.newUserDao()).thenReturn(userDao);

        final Optional<User> found = userService.findUserById(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testFindUserByLogin(User user) throws DBException {
        when(userDao.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(daoFactory.newUserDao()).thenReturn(userDao);

        final Optional<User> found = userService.findUserByLogin(user.getLogin());
        assertTrue(found.isPresent());
        assertEquals(user, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testFindUserPassword(User user) throws DBException {
        final UserPassword password = UserPassword.hash("pass" + user.getLogin());
        when(userPasswordDao.findByKey(user.getId())).thenReturn(Optional.of(password));
        when(daoFactory.newUserPasswordDao()).thenReturn(userPasswordDao);

        final Optional<UserPassword> found = userService.findUserPassword(user.getId());
        assertTrue(found.isPresent());
        assertEquals(password, found.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testAuthenticate(User user) throws DBException {
        when(userDao.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(daoFactory.newUserDao()).thenReturn(userDao);

        final String password = "pass" + user.getLogin();
        final UserPassword hashedPassword = UserPassword.hash(password);
        hashedPassword.setUserId(user.getId());
        when(userPasswordDao.findByKey(user.getId())).thenReturn(Optional.of(hashedPassword));
        when(daoFactory.newUserPasswordDao()).thenReturn(userPasswordDao);

        final Optional<User> authenticated = userService.authenticate(user.getLogin(), password);
        assertTrue(authenticated.isPresent());
        assertEquals(user, authenticated.orElseThrow());
    }

    @Test
    public void testUpdateUserStatus() throws DBException {
        final User user = mock(User.class);
        when(user.getStatus()).thenReturn(User.Status.ACTIVE);

        when(userDao.findByKey(user.getId())).thenReturn(Optional.of(user));
        when(userDao.update(user)).thenReturn(true);
        when(daoFactory.newUserDao()).thenReturn(userDao);

        for (var status : User.Status.values()) {
            if (user.getStatus() != status) {
                final boolean updated = userService.updateUserStatus(user.getId(), status);
                when(user.getStatus()).thenReturn(status);
                assertTrue(updated);
                verify(user).setStatus(status);
                verify(userDao).update(user);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsertUser(User user) throws DBException, ValidationException {
        when(userDao.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(userDao.insert(user)).thenReturn(true);
        when(daoFactory.newUserDao()).thenReturn(userDao);

        final String password = "pass" + user.getLogin();
        when(userPasswordDao.insert(any())).thenReturn(true);
        when(daoFactory.newUserPasswordDao()).thenReturn(userPasswordDao);

        final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
        when(userAccountDao.insert(userAccount)).thenReturn(true);
        when(daoFactory.newUserAccountDao()).thenReturn(userAccountDao);

        final boolean inserted = userService.insertUser(user, password);
        assertTrue(inserted);
        verify(userDao).insert(user);
        verify(userPasswordDao).insert(any());
        verify(userAccountDao).insert(userAccount);
    }
}
