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
    }
}
