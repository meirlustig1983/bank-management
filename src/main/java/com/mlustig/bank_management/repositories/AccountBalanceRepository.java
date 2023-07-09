package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.AccountBalance;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    Optional<AccountBalance> findByAccountInfo_UserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE AccountBalance ab SET ab.balance = :balance WHERE ab.accountInfo IN (SELECT ai from AccountInfo ai WHERE ai.userName = :userName)")
    int updateBalanceByUserName(@Param("userName") String userName, @Param("balance") BigDecimal balance);
}