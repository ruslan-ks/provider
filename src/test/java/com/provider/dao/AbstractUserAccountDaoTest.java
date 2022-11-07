package com.provider.dao;

import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserAccountDaoTest extends AbstractDaoTest {
    protected static UserAccountDao userAccountDao;
    protected static UserDao userDao;

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testInsert(User user) throws DBException {
        userDao.insert(user);

        final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
        final boolean inserted = userAccountDao.insert(userAccount);
        assertTrue(inserted);
        assertNotEquals(0, userAccount.getId());
        assertEquals(user.getId(), userAccount.getUserId());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testFindByKey(User user) throws DBException {
        userDao.insert(user);

        final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
        userAccountDao.insert(userAccount);

        final Optional<UserAccount> userAccountOptional = userAccountDao.findByKey(userAccount.getId());
        assertTrue(userAccountOptional.isPresent());
        assertEquals(userAccount, userAccountOptional.orElseThrow());
        assertEquals(user.getId(), userAccountOptional.orElseThrow().getUserId());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testUpdate(User user) throws DBException {
        userDao.insert(user);

        final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
        userAccountDao.insert(userAccount);

        userAccount.replenish(BigDecimal.valueOf(1000));
        userAccountDao.update(userAccount);

        Optional<UserAccount> userAccountOptional = userAccountDao.findByKey(userAccount.getId());
        assertEquals(userAccount, userAccountOptional.orElseThrow());

        userAccount.withdraw(BigDecimal.valueOf(500));
        userAccountDao.update(userAccount);

        userAccountOptional = userAccountDao.findByKey(userAccount.getId());
        assertEquals(userAccount, userAccountOptional.orElseThrow());
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#userStream")
    public void testFindAll(User user) throws DBException {
        userDao.insert(user);

        final UserAccount userAccount = entityFactory.newUserAccount(0, user.getId(), Currency.USD);
        userAccountDao.insert(userAccount);

        final List<UserAccount> foundAccounts = userAccountDao.findAll(user.getId());
        assertTrue(foundAccounts.contains(userAccount));
        assertEquals(userAccount, foundAccounts.iterator().next());
    }
}
