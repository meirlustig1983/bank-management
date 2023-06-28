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

    private Counter getAccountInfoCounter;
    private Counter createAccountCounter;
    private Counter deleteBankAccountCounter;
    private Timer getAccountInfoTimer;
    private Timer createAccountTimer;
    private Timer deleteBankAccountTimer;

    @PostConstruct
    public void init() {

        getAccountInfoCounter = Counter.builder("bank_account_get_account_info_counter")
                .description("Number of times getAccountInfo method has been called")
                .register(meterRegistry);
        createAccountCounter = Counter.builder("bank_account_create_account_counter")
                .description("Number of times createAccount method has been called")
                .register(meterRegistry);
        deleteBankAccountCounter = Counter.builder("bank_account_delete_bank_account_counter")
                .description("Number of times deleteBankAccount method has been called")
                .register(meterRegistry);

        getAccountInfoTimer = Timer.builder("bank_account_get_account_info_timer")
                .description("Execution time of getAccountInfo method")
                .register(meterRegistry);
        createAccountTimer = Timer.builder("bank_account_create_account_timer")
                .description("Execution time of createAccount method")
                .register(meterRegistry);
        deleteBankAccountTimer = Timer.builder("bank_account_delete_bank_account_timer")
                .description("Execution time of deleteBankAccount method")
                .register(meterRegistry);
    }
}
