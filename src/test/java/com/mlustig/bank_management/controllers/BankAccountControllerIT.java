package com.mlustig.bank_management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.requests.TransactionRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createFirstAccount() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("john.doe@gmail.com", "John", "Doe", BigDecimal.valueOf(4500), BigDecimal.valueOf(1500), false, List.of());
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void createSecondAccount() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("meir.lustig@gmail.com", "Meir", "Lustig", BigDecimal.valueOf(45000), BigDecimal.valueOf(-1500), false, List.of());
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void createAccountWithWrongFormatAccountId() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("johndoe", "John", "Doe", BigDecimal.valueOf(4500), BigDecimal.valueOf(1500), false, List.of());
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts"))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void createFirstAccountTwice() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("john.doe@gmail.com", "John", "Doe", BigDecimal.valueOf(4500), BigDecimal.valueOf(1500), false, List.of());
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts"))
                .andExpect(jsonPath("$.message").value("Internal SQL error"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(5)
    void getAccountInfoForFirstAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }


    @Test
    @Order(6)
    void getAccountInfoForSecondAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(7)
    void getAccountInfoForWrongFormatAccountId() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doegmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/john.doegmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(8)
    void getAccountInfoForNoExistsAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/no.exists@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(9)
    void makeDepositToInactiveAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("john.doe@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(10)
    void makeWithdrawFromInactiveAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(11)
    void makeDepositWithWrongFormatAccountId() throws Exception {
        TransactionRequest request = new TransactionRequest("john.doegmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: accountId]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(12)
    void makeWithdrawWithWrongFormatAccountId() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustiggmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: accountId]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(13)
    void makeDepositWithNoExistsAccountId() throws Exception {
        TransactionRequest request = new TransactionRequest("no.exists@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(14)
    void makeWithdrawWithNoExistsAccountId() throws Exception {
        TransactionRequest request = new TransactionRequest("no.exists@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(15)
    void activateFirstAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/activate", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(16)
    void activateSecondAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/activate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(17)
    void activateWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/activate", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/meir.lustiggmail.com/activate"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(18)
    void activateWithNoExistsAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/activate", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/no.exists@gmail.com/activate"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(19)
    void makeDepositToFirstAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("john.doe@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(5000))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(20)
    void makeDepositToSecondAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45500))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(21)
    void makeWithdrawFromSecondAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 47000);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(-1500))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(22)
    void makeWithdrawFromSecondAccountOverTheMinimum() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 1);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Insufficient funds exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(23)
    void makeWithdrawFromSecondAccountWithNegativeAmount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", -1);
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: amount]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(24)
    void makeDepositFromSecondAccountWithNegativeAmount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", -1);
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: amount]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(25)
    void deactivateAccountFirstAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/deactivate", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(5000.00))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(26)
    void deactivateAccountSecondAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/deactivate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(-1500.0))
                .andExpect(jsonPath("$.minimumBalance").value(-1500.0))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions").isEmpty())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(27)
    void deactivateAccountWithNoExistsAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/deactivate", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/no.exists@gmail.com/deactivate"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(28)
    void deactivateAccountWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/{accountId}/deactivate", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/meir.lustiggmail.com/deactivate"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(29)
    void getAccountInfoForFirstAccountAfterDeactivate() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(-1500))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.transactions.length()").value(2))  // Verify the number of transactions
                .andExpect(jsonPath("$.transactions[0].amount").value(500))  // Verify the amount of the first transaction
                .andExpect(jsonPath("$.transactions[0].type").value("DEPOSIT"))  // Verify the type of the first transaction
                .andExpect(jsonPath("$.transactions[1].amount").value(47000))  // Verify the amount of the second transaction
                .andExpect(jsonPath("$.transactions[1].type").value("WITHDRAW"))  // Verify the type of the second transaction
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(30)
    void deleteFirstBankAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}"));

        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/john.doe@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    @Order(31)
    void deleteSecondBankAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}"));

        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    @Order(32)
    void deleteBankAccountWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/meir.lustiggmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(33)
    void deleteBankAccountWithNoExistsAccountId() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "no.exists@gmail.com"))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(34)
    void makeWithdrawWithWrongFieldType() throws Exception {

        HashMap<String, String> request = new HashMap<>();
        request.put("accountId", "meir.lustig@gmail.com");
        request.put("amount", "meir.lustig@gmail.com");

        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Wrong field type exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }
}