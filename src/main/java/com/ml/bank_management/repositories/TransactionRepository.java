package com.ml.bank_management.repositories;

import com.ml.bank_management.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}