package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    Optional<AccountBalance> findByAccountInfo_UserName(String userName);
}