package com.mlustig.bank_management.config;

import com.mlustig.bank_management.mappers.BankAccountMapper;
import com.mlustig.bank_management.mappers.BankAccountStatusMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public BankAccountMapper bankAccountMapper() {
        return BankAccountMapper.INSTANCE;
    }

    @Bean
    public BankAccountStatusMapper bankAccountStatusMapper() {
        return BankAccountStatusMapper.INSTANCE;
    }
}
