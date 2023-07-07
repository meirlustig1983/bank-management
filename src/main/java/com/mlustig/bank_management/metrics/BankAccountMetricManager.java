package com.mlustig.bank_management.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class BankAccountMetricManager {

    private final MeterRegistry meterRegistry;

    private Counter getAccountInfoByUserNameCounter;
    private Counter getAccountInfoByEmailCounter;
    private Counter getAccountInfoByPhoneNumberCounter;
    private Counter createAccountCounter;
    private Counter deleteBankAccountCounter;
    private Timer getAccountInfoByUserNameTimer;
    private Timer getAccountInfoByEmailTimer;
    private Timer getAccountInfoByPhoneNumberTimer;
    private Timer createAccountTimer;
    private Timer deleteBankAccountTimer;

    @PostConstruct
    public void init() {

        getAccountInfoByUserNameCounter = Counter.builder("bank_account_get_account_info_by_user_name_counter")
                .description("Number of times getAccountInfoByUserName method has been called")
                .register(meterRegistry);
        getAccountInfoByEmailCounter = Counter.builder("bank_account_get_account_info_by_email_counter")
                .description("Number of times getAccountInfoByEmail method has been called")
                .register(meterRegistry);
        getAccountInfoByPhoneNumberCounter = Counter.builder("bank_account_get_account_info_by_phone_number_counter")
                .description("Number of times getAccountInfoByPhoneNumber method has been called")
                .register(meterRegistry);
        createAccountCounter = Counter.builder("bank_account_create_account_counter")
                .description("Number of times createAccount method has been called")
                .register(meterRegistry);
        deleteBankAccountCounter = Counter.builder("bank_account_delete_bank_account_counter")
                .description("Number of times deleteBankAccount method has been called")
                .register(meterRegistry);

        getAccountInfoByUserNameTimer = Timer.builder("bank_account_get_account_info_by_user_name_timer")
                .description("Execution time of getAccountInfoByUserName method")
                .register(meterRegistry);
        getAccountInfoByEmailTimer = Timer.builder("bank_account_get_account_info_by_email_timer")
                .description("Execution time of getAccountInfoByEmail method")
                .register(meterRegistry);
        getAccountInfoByPhoneNumberTimer = Timer.builder("bank_account_get_account_info_by_phone_number_timer")
                .description("Execution time of getAccountInfoByPhoneNumber method")
                .register(meterRegistry);
        createAccountTimer = Timer.builder("bank_account_create_account_timer")
                .description("Execution time of createAccount method")
                .register(meterRegistry);
        deleteBankAccountTimer = Timer.builder("bank_account_delete_bank_account_timer")
                .description("Execution time of deleteBankAccount method")
                .register(meterRegistry);
    }
}
