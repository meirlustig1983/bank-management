package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
    @Query("SELECT ai FROM AccountInfo ai WHERE ai.userName = :userName")
    Optional<AccountInfo> findByUserName(@Param("userName") String userName);

    @Query("SELECT ai FROM AccountInfo ai WHERE ai.email = :email")
    Optional<AccountInfo> findByEmail(@Param("email") String email);

    @Query("SELECT ai FROM AccountInfo ai WHERE ai.phoneNumber = :email")
    Optional<AccountInfo> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query("DELETE FROM AccountInfo ai WHERE ai.userName = :userName")
    void deleteByUserName(@Param("userName") String userName);
}