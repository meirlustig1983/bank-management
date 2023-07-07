package com.mlustig.bank_management.repositories;

import com.mlustig.bank_management.dao.AccountProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountPropertiesRepository extends JpaRepository<AccountProperties, Long> {
    Optional<AccountProperties> findByAccountInfo_UserName(String userName);
}