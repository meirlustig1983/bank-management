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
public class BankAccountMeterRegistry {

    private final MeterRegistry meterRegistry;

    private Counter getAccountInfoCounter;
    private Counter createAccountCounter;
    private Counter deleteBankAccountCounter;
    private Counter activateAccountCounter;
    private Counter deactivateAccountCounter;
    private Counter makeDepositCounter;
    private Counter makeWithdrawCounter;
    private Timer getAccountInfoTimer;
    private Timer createAccountTimer;
    private Timer deleteBankAccountTimer;
    private Timer activateAccountTimer;
    private Timer deactivateAccountTimer;
    private Timer makeDepositTimer;
    private Timer makeWithdrawTimer;

    @PostConstruct
    public void init() {

        getAccountInfoCounter = Counter.builder("bank_account_controller_get_account_info_counter")
                .description("Number of times getAccountInfo method has been called")
                .register(meterRegistry);
        createAccountCounter = Counter.builder("bank_account_controller_create_account_counter")
                .description("Number of times createAccount method has been called")
                .register(meterRegistry);
        deleteBankAccountCounter = Counter.builder("bank_account_controller_delete_bank_account_counter")
                .description("Number of times deleteBankAccount method has been called")
                .register(meterRegistry);
        activateAccountCounter = Counter.builder("bank_account_controller_activate_account_counter")
                .description("Number of times activateAccount method has been called")
                .register(meterRegistry);
        deactivateAccountCounter = Counter.builder("bank_account_controller_deactivate_account_counter")
                .description("Number of times deactivateAccount method has been called")
                .register(meterRegistry);
        makeDepositCounter = Counter.builder("bank_account_controller_make_deposit_counter")
                .description("Number of times makeDeposit method has been called")
                .register(meterRegistry);
        makeWithdrawCounter = Counter.builder("bank_account_controller_make_withdraw_counter")
                .description("Number of times makeWithdraw method has been called")
                .register(meterRegistry);

        getAccountInfoTimer = Timer.builder("bank_account_controller_get_account_info_timer")
                .description("Execution time of getAccountInfo method")
                .register(meterRegistry);
        createAccountTimer = Timer.builder("bank_account_controller_create_account_timer")
                .description("Execution time of createAccount method")
                .register(meterRegistry);
        deleteBankAccountTimer = Timer.builder("bank_account_controller_delete_bank_account_timer")
                .description("Execution time of deleteBankAccount method")
                .register(meterRegistry);
        activateAccountTimer = Timer.builder("bank_account_controller_activate_account_timer")
                .description("Execution time of activateAccount method")
                .register(meterRegistry);
        deactivateAccountTimer = Timer.builder("bank_account_controller_deactivate_account_timer")
                .description("Execution time of deactivateAccount method")
                .register(meterRegistry);
        makeDepositTimer = Timer.builder("bank_account_controller_make_deposit_timer")
                .description("Execution time of makeDeposit method")
                .register(meterRegistry);
        makeWithdrawTimer = Timer.builder("bank_account_controller_make_withdraw_timer")
                .description("Execution time of makeWithdraw method")
                .register(meterRegistry);


    }
}
