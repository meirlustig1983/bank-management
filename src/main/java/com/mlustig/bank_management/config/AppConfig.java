package com.mlustig.bank_management.config;

import com.mlustig.bank_management.mappers.AccountBalanceMapper;
import com.mlustig.bank_management.mappers.AccountInfoMapper;
import com.mlustig.bank_management.mappers.AccountPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AccountInfoMapper accountInfoMapper() {
        return AccountInfoMapper.INSTANCE;
    }

    @Bean
    public AccountPropertiesMapper accountPropertiesMapper() {
        return AccountPropertiesMapper.INSTANCE;
    }

    @Bean
    public AccountBalanceMapper accountBalanceMapper() {
        return AccountBalanceMapper.INSTANCE;
    }
}
