package com.mlustig.bank_management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlustig.bank_management.dto.AccountInfoDto;
import com.mlustig.bank_management.requests.CreditLimitRequest;
import com.mlustig.bank_management.requests.TransactionRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankManagementControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createAccount() throws Exception {
        AccountInfoDto accountInfo = new AccountInfoDto("meir.lustig@gmail.com",
                "Meir",
                "Lustig",
                "meir.lustig@gmail.com",
                "480-111-2233");

        mockMvc.perform(post("/api/v1/bank/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountInfo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.email").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("480-111-2233"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void makeDepositToInactiveAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/deposit"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeWithdrawFromInactiveAccount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void makeDepositWithWrongFormatUserName() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustiggmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/deposit"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: userName]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(5)
    void makeWithdrawWithWrongFormatUserName() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustiggmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: userName]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(6)
    void makeDepositWithNoExistsUserName() throws Exception {
        TransactionRequest request = new TransactionRequest("no.exists@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/deposit"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(7)
    void makeWithdrawWithNoExistsUserName() throws Exception {
        TransactionRequest request = new TransactionRequest("no.exists@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(8)
    void activateAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/activate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.active").value("true"))
                .andExpect(jsonPath("$.creditLimit").value("0.0"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(9)
    void activateWithWrongFormatUserName() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/activate", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/meir.lustiggmail.com/activate"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(10)
    void activateWithNoExistsUserName() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/activate", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/no.exists@gmail.com/activate"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(11)
    void activateActiveAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/activate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.active").value("true"))
                .andExpect(jsonPath("$.creditLimit").value("0.0"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(12)
    void makeDeposit() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value("500.0"))
                .andExpect(jsonPath("$.updatedAt").exists())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(13)
    void makeWithdraw() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 500);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value("0.0"))
                .andExpect(jsonPath("$.updatedAt").exists())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(14)
    void makeWithdrawOverTheMinimum() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", 1);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Insufficient funds exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(15)
    void updateCreditLimit() throws Exception {
        CreditLimitRequest request = new CreditLimitRequest("meir.lustig@gmail.com", -1000);
        mockMvc.perform(post("/api/v1/bank/management/credit-limit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditLimit").value("-1000.0"))
                .andExpect(jsonPath("$.active").value("true"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(15)
    void makeWithdrawWithNegativeAmount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", -1);
        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: amount]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(16)
    void makeDepositWithNegativeAmount() throws Exception {
        TransactionRequest request = new TransactionRequest("meir.lustig@gmail.com", -1);
        mockMvc.perform(post("/api/v1/bank/management/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/deposit"))
                .andExpect(jsonPath("$.message").value("Request validation exception [field: amount]"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(17)
    void deactivateAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/deactivate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.active").value("false"))
                .andExpect(jsonPath("$.creditLimit").value("-1000.0"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(18)
    void deactivateAccountWithNoExistsUserName() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/deactivate", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/no.exists@gmail.com/deactivate"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(19)
    void deactivateAccountWithWrongFormatUserName() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/deactivate", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/meir.lustiggmail.com/deactivate"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(20)
    void deactivateInactiveAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank/management/{userName}/deactivate", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.active").value("false"))
                .andExpect(jsonPath("$.creditLimit").value("-1000.0"))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(21)
    void makeWithdrawWithWrongFieldType() throws Exception {

        HashMap<String, String> request = new HashMap<>();
        request.put("userName", "meir.lustig@gmail.com");
        request.put("amount", "meir.lustig@gmail.com");

        mockMvc.perform(post("/api/v1/bank/management/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/management/withdraw"))
                .andExpect(jsonPath("$.message").value("Wrong field type exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }
}