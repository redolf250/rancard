package com.redolf.rancard.repositories;

import com.redolf.rancard.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, Integer> {
}
