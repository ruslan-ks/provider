package com.provider.dao.transaction;

import com.provider.dao.EntityDao;
import com.provider.dao.exception.DBException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class TransactionTest {
    @Test
    public void testCreateTransaction() throws DBException, SQLException {
        final List<? extends EntityDao<?, ?>> daos = Stream.generate(() -> (EntityDao<?, ?>) mock(EntityDao.class))
                .limit(8)
                .toList();
        final Connection connection = mock(Connection.class);
        final Transaction transaction = Transaction.of(connection, daos.toArray(new EntityDao[]{}));
        verify(connection).setAutoCommit(false);
        for (var dao : daos) {
            verify(dao).setConnection(connection);
        }

        transaction.close();

        verify(connection).setAutoCommit(true);
        verify(connection).close();
        for (var dao : daos) {
            verify(dao).resetConnection();
        }
    }

    @Test
    public void testCommitTransaction() throws DBException, SQLException {
        final Connection connection = mock(Connection.class);
        final Transaction transaction = Transaction.of(connection);
        transaction.commit();
        verify(connection).commit();
    }

    @Test
    public void testRollbackTransaction() throws DBException, SQLException {
        final Connection connection = mock(Connection.class);
        final Transaction transaction = Transaction.of(connection);
        transaction.rollback();
        verify(connection).rollback();
    }
}
