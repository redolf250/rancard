package com.redolf.rancard.services.interfaces;

import com.redolf.rancard.exceptions.TransactionNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransactionService<T,U> {

    public U createTransaction(T entity);

    public U updateTransaction(int id,T entity);

    public U findTransactionById(int id) throws TransactionNotFoundException;

    public void deleteTransactionById(int id);

    List<U> getTransactionsWithPageSize(int pageSize);

    List<U> getAllTransactions();

    List<U> getTransactionsWithAndOffSetPageSize( int offSet,int pageSize);
}
