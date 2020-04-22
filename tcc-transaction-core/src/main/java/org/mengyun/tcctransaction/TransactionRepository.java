package org.mengyun.tcctransaction;

import org.mengyun.tcctransaction.api.TransactionXid;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by changmingxie on 11/12/15.
 * 自定义的事务操作接口，常规的事务操作
 */
public interface TransactionRepository {

    int create(Transaction transaction);

    int update(Transaction transaction);

    int delete(Transaction transaction);

    Transaction findByXid(TransactionXid xid);

    List<Transaction> findAllUnmodifiedSince(Date date);
}
