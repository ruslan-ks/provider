package com.provider.service;

import com.provider.dao.UserAccountDao;
import com.provider.dao.exception.DBException;
import com.provider.entity.Currency;
import com.provider.entity.user.User;
import com.provider.entity.user.UserAccount;
import com.provider.service.exception.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class AbstractAccountServiceTest extends AbstractServiceTest {
    /**
     * AccountService implementation under testing
     */
    protected AccountService accountService;

    @Mock
    private UserAccountDao userAccountDao;

    @Nested
    class Positive {
        @ParameterizedTest
        @MethodSource("com.provider.TestData#userStream")
        public void testReplenish(User user) throws DBException, ValidationException {
            final UserAccount userAccount = entityFactory.newUserAccount(1, user.getId(), Currency.USD);
            final BigDecimal amount = BigDecimal.valueOf(user.getId() * 11);
            when(userAccountDao.update(userAccount)).thenReturn(true);
            when(daoFactory.newUserAccountDao()).thenReturn(userAccountDao);

            assertTrue(accountService.replenish(userAccount, amount));
            assertEquals(amount, userAccount.getAmount());

            verify(userAccountDao).update(userAccount);
        }

        @ParameterizedTest
        @MethodSource("com.provider.TestData#userStream")
        public void testFindUserAccount(User user) throws DBException {
            final UserAccount userAccount = entityFactory.newUserAccount(1, user.getId(), Currency.USD);
            userAccount.replenish(BigDecimal.valueOf(user.getId() * 11 + 10));
            final List<UserAccount> userAccountList = List.of(userAccount);
            when(userAccountDao.findAll(user.getId())).thenReturn(userAccountList);
            when(daoFactory.newUserAccountDao()).thenReturn(userAccountDao);

            final var found = accountService.findUserAccount(user);
            assertEquals(userAccount, found);
        }
    }

    @Nested
    class Negative {
        @ParameterizedTest
        @MethodSource("com.provider.TestData#userStream")
        public void testReplenishWithNegativeAmount(User user) throws DBException {
            final UserAccount userAccount = entityFactory.newUserAccount(1, user.getId(), Currency.USD);
            final BigDecimal amount = BigDecimal.valueOf(-user.getId() * 11 -100);
            when(daoFactory.newUserAccountDao()).thenReturn(userAccountDao);

            assertThrows(ValidationException.class, () -> accountService.replenish(userAccount, amount));

            verify(userAccountDao, never()).update(userAccount);
        }
    }
}
