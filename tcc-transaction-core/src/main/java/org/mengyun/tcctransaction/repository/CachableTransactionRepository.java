package org.mengyun.tcctransaction.repository;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.mengyun.tcctransaction.ConcurrentTransactionException;
import org.mengyun.tcctransaction.OptimisticLockException;
import org.mengyun.tcctransaction.Transaction;
import org.mengyun.tcctransaction.TransactionRepository;
import org.mengyun.tcctransaction.api.TransactionXid;

import javax.transaction.xa.Xid;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by changmingxie on 10/30/15.
 */
public abstract class CachableTransactionRepository implements TransactionRepository {

    private int expireDuration = 120;

    /**
     * 用来缓存对应的事务，目前没看到啥用
     */
    private Cache<Xid, Transaction> transactionXidCompensableTransactionCache;

    @Override
    public int create(Transaction transaction) {
        int result = doCreate(transaction);
        if (result > 0) {
            putToCache(transaction);
        } else {
            throw new ConcurrentTransactionException("transaction xid duplicated. xid:" + transaction.getXid().toString());
        }

        return result;
    }

    @Override
    public int update(Transaction transaction) {
        int result = 0;

        try {
            result = doUpdate(transaction);
            if (result > 0) {
                putToCache(transaction);
            } else {
                throw new OptimisticLockException();
            }
        } finally {
            if (result <= 0) {
                removeFromCache(transaction);
            }
        }

        return result;
    }

    @Override
    public int delete(Transaction transaction) {
        int result = 0;

        try {
            result = doDelete(transaction);

        } finally {
            removeFromCache(transaction);
        }
        return result;
    }

    @Override
    public Transaction findByXid(TransactionXid transactionXid) {
        Transaction transaction = findFromCache(transactionXid);

        if (transaction == null) {
            transaction = doFindOne(transactionXid);

            if (transaction != null) {
                putToCache(transaction);
            }
        }

        return transaction;
    }

    @Override
    public List<Transaction> findAllUnmodifiedSince(Date date) {

        List<Transaction> transactions = doFindAllUnmodifiedSince(date);

        for (Transaction transaction : transactions) {
            //这里放入缓存的是，可能是重启后需要重数据中加载对应的事务信息放进缓存中
            putToCache(transaction);
        }

        return transactions;
    }

    public CachableTransactionRepository() {
        transactionXidCompensableTransactionCache = CacheBuilder.newBuilder().expireAfterAccess(expireDuration, TimeUnit.SECONDS).maximumSize(1000).build();
    }

    protected void putToCache(Transaction transaction) {
        transactionXidCompensableTransactionCache.put(transaction.getXid(), transaction);
    }

    protected void removeFromCache(Transaction transaction) {
        transactionXidCompensableTransactionCache.invalidate(transaction.getXid());
    }

    protected Transaction findFromCache(TransactionXid transactionXid) {
        return transactionXidCompensableTransactionCache.getIfPresent(transactionXid);
    }

    public void setExpireDuration(int durationInSeconds) {
        this.expireDuration = durationInSeconds;
    }

    protected abstract int doCreate(Transaction transaction);

    protected abstract int doUpdate(Transaction transaction);

    protected abstract int doDelete(Transaction transaction);

    protected abstract Transaction doFindOne(Xid xid);

    protected abstract List<Transaction> doFindAllUnmodifiedSince(Date date);
}
