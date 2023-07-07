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
public class BankManagementMetricManager {

    private final MeterRegistry meterRegistry;

    private Counter activateAccountCounter;
    private Counter deactivateAccountCounter;
    private Counter makeDepositCounter;
    private Counter makeWithdrawCounter;
    private Counter getBalanceCounter;
    private Timer activateAccountTimer;
    private Timer deactivateAccountTimer;
    private Timer makeDepositTimer;
    private Timer makeWithdrawTimer;
    private Timer getBalanceTimer;

    @PostConstruct
    public void init() {
        activateAccountCounter = Counter.builder("bank_management_activate_account_counter")
                .description("Number of times activateAccount method has been called")
                .register(meterRegistry);
        deactivateAccountCounter = Counter.builder("bank_management_deactivate_account_counter")
                .description("Number of times deactivateAccount method has been called")
                .register(meterRegistry);
        makeDepositCounter = Counter.builder("bank_management_make_deposit_counter")
                .description("Number of times makeDeposit method has been called")
                .register(meterRegistry);
        makeWithdrawCounter = Counter.builder("bank_management_make_withdraw_counter")
                .description("Number of times makeWithdraw method has been called")
                .register(meterRegistry);
        getBalanceCounter = Counter.builder("bank_management_get_balance_counter")
                .description("Number of times getBalance method has been called")
                .register(meterRegistry);

        activateAccountTimer = Timer.builder("bank_management_activate_account_timer")
                .description("Execution time of activateAccount method")
                .register(meterRegistry);
        deactivateAccountTimer = Timer.builder("bank_management_deactivate_account_timer")
                .description("Execution time of deactivateAccount method")
                .register(meterRegistry);
        makeDepositTimer = Timer.builder("bank_management_make_deposit_timer")
                .description("Execution time of makeDeposit method")
                .register(meterRegistry);
        makeWithdrawTimer = Timer.builder("bank_management_make_withdraw_timer")
                .description("Execution time of makeWithdraw method")
                .register(meterRegistry);
        getBalanceTimer = Timer.builder("bank_management_get_balance_timer")
                .description("Execution time of getBalance method")
                .register(meterRegistry);

    }
}
