package by.shimakser.config;

import com.gruelbox.transactionoutbox.Persistor;
import com.gruelbox.transactionoutbox.Transaction;
import com.gruelbox.transactionoutbox.TransactionManager;
import com.gruelbox.transactionoutbox.TransactionOutboxEntry;

import java.time.Instant;
import java.util.List;

public class CustomPersistor implements Persistor {
    public CustomPersistor(Object dialect, Object migrate, Object buildInvocationSerializer) {
    }

    @Override
    public void migrate(TransactionManager transactionManager) {

    }

    @Override
    public void save(Transaction transaction, TransactionOutboxEntry transactionOutboxEntry) throws Exception {

    }

    @Override
    public void delete(Transaction transaction, TransactionOutboxEntry transactionOutboxEntry) throws Exception {

    }

    @Override
    public void update(Transaction transaction, TransactionOutboxEntry transactionOutboxEntry) throws Exception {

    }

    @Override
    public boolean lock(Transaction transaction, TransactionOutboxEntry transactionOutboxEntry) throws Exception {
        return false;
    }

    @Override
    public boolean unblock(Transaction transaction, String s) throws Exception {
        return false;
    }

    @Override
    public List<TransactionOutboxEntry> selectBatch(Transaction transaction, int i, Instant instant) throws Exception {
        return null;
    }

    @Override
    public int deleteProcessedAndExpired(Transaction transaction, int i, Instant instant) throws Exception {
        return 0;
    }
}
