package com.ml.bank_management.config;

import com.ml.bank_management.mappers.BankAccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public BankAccountMapper bankAccountMapper() {
        return BankAccountMapper.INSTANCE;
    }
}
