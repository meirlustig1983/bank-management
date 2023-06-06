package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountId = :accountId")
    Optional<BankAccount> findBankAccountByAccountId(@Param("accountId") String accountId);

    @Modifying
    @Query("DELETE FROM BankAccount ba WHERE ba.accountId = :accountId")
    void deleteByAccountId(@Param("accountId") String accountId);
}