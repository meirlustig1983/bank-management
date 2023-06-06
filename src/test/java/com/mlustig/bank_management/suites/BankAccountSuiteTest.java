package com.mlustig.bank_management.suites;

import com.mlustig.bank_management.controllers.BankAccountControllerIT;
import com.mlustig.bank_management.facades.DataFacadeIT;
import com.mlustig.bank_management.services.BankAccountServiceIT;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({DataFacadeIT.class, BankAccountServiceIT.class, BankAccountControllerIT.class})
public class BankAccountSuiteTest {
    // intentionally empty
}