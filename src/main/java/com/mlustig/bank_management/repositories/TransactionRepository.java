package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountInfo_UserName(String userName);
}