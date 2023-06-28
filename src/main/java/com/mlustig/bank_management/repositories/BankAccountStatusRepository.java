package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.BankAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountStatusRepository extends JpaRepository<BankAccountStatus, Long> {
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountId = :accountId")
    Optional<BankAccountStatus> findBankAccountStatusByAccountId(@Param("accountId") String accountId);
}