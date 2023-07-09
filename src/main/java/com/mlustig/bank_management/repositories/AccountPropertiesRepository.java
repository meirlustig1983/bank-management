package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.AccountProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountPropertiesRepository extends JpaRepository<AccountProperties, Long> {

    Optional<AccountProperties> findByAccountInfo_UserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE AccountProperties ap SET ap.active = :active WHERE ap.accountInfo IN (SELECT ai from AccountInfo ai WHERE ai.userName = :userName)")
    int updateActiveStatusByUserName(@Param("userName") String userName, @Param("active") boolean active);

    @Modifying
    @Transactional
    @Query("UPDATE AccountProperties ap SET ap.creditLimit = :creditLimit WHERE ap.accountInfo IN (SELECT ai from AccountInfo ai WHERE ai.userName = :userName)")
    int updateCreditLimitByUserName(@Param("userName") String userName, @Param("creditLimit") BigDecimal creditLimit);
}