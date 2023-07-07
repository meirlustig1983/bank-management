package com.mlustig.bank_management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlustig.bank_management.dto.AccountInfoDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Tag("integration")
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
    void createAccount() throws Exception {
        AccountInfoDto accountInfo = new AccountInfoDto("meir.lustig@gmail.com",
                "Meir",
                "Lustig",
                "meir.lustig@gmail.com",
                "480-111-2233",
                LocalDateTime.now());

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
                .andExpect(jsonPath("$.updatedAt").isString())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void createAccountWithWrongFormatAccountId() throws Exception {
        AccountInfoDto accountInfo = new AccountInfoDto("meir.lustig",
                "Meir",
                "Lustig",
                "meir.lustig@gmail.com",
                "480-111-2233",
                LocalDateTime.now());
        mockMvc.perform(post("/api/v1/bank/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account"))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void createAccountTwice() throws Exception {
        AccountInfoDto accountInfo = new AccountInfoDto("meir.lustig@gmail.com",
                "Meir",
                "Lustig",
                "meir.lustig@gmail.com",
                "480-111-2233",
                LocalDateTime.now());
        mockMvc.perform(post("/api/v1/bank/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountInfo)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account"))
                .andExpect(jsonPath("$.message").value("Internal SQL error"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void getAccountInfoByUserName() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/{userName}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.email").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("480-111-2233"))
                .andExpect(jsonPath("$.updatedAt").isString())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(5)
    void getAccountInfoByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/email?value={email}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.email").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("480-111-2233"))
                .andExpect(jsonPath("$.updatedAt").isString())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(6)
    void getAccountInfoByPhoneNumber() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/phone-number?value={userName}", "480-111-2233"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.email").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("480-111-2233"))
                .andExpect(jsonPath("$.updatedAt").isString())
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(7)
    void getAccountInfoWithWrongFormatUserName() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/{userName}", "meir.lustig"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/meir.lustig"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(8)
    void getAccountInfoWithWrongFormatEmail() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/email?value={email}", "meir.lustig"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/email"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(9)
    void getAccountInfoWithNotExistsAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/{userName}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/no.exists@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(10)
    void getAccountInfoWithNotExistsEmail() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/email?value={email}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/email"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(11)
    void getAccountInfoWithNotExistsPhoneNumber() throws Exception {
        mockMvc.perform(get("/api/v1/bank/account/phone-number?value={phone}", "111-111-1111"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/phone-number"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }


    @Test
    @Order(12)
    void deleteBankAccountWithWrongFormatUserName() throws Exception {
        mockMvc.perform(delete("/api/v1/bank/account/{userName}", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/meir.lustiggmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(13)
    void deleteBankAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/bank/account/{userName}", "meir.lustig@gmail.com"))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}"));

        mockMvc.perform(get("/api/v1/bank/account/{userName}", "meir.lustig@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank/account/meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    @Order(14)
    void deleteBankAccountWithNotExistsAccountId() throws Exception {
        mockMvc.perform(delete("/api/v1/bank/account/{userName}", "meir.lustig@gmail.com"))
                .andExpect(status().isNoContent())
                .andDo(document("{method-name}"));
    }
}