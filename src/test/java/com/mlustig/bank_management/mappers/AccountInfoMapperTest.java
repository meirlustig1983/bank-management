package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.AccountInfo;
import com.mlustig.bank_management.dto.AccountInfoDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountInfoMapperTest {

    @Test
    void toDto() {
        AccountInfo accountInfo = AccountInfo.builder()
                .accountInfoId(1L)
                .userName("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .email("theodore.roosevelt@gmail.com")
                .phoneNumber("111-222-3344")
                .build();

        AccountInfoDto accountInfoDto = AccountInfoMapper.INSTANCE.toDto(accountInfo);

        assertThat(accountInfoDto.userName()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(accountInfoDto.firstName()).isEqualTo("Theodore");
        assertThat(accountInfoDto.lastName()).isEqualTo("Roosevelt");
        assertThat(accountInfoDto.email()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(accountInfoDto.phoneNumber()).isEqualTo("111-222-3344");
    }

    @Test
    void toDao() {

        AccountInfoDto accountInfoDto = new AccountInfoDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                "theodore.roosevelt@gmail.com", "111-222-3344");

        AccountInfo accountInfo = AccountInfoMapper.INSTANCE.toDao(accountInfoDto);

        assertThat(accountInfo.getAccountInfoId()).isNull();
        assertThat(accountInfo.getUserName()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(accountInfo.getFirstName()).isEqualTo("Theodore");
        assertThat(accountInfo.getLastName()).isEqualTo("Roosevelt");
        assertThat(accountInfo.getEmail()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(accountInfo.getPhoneNumber()).isEqualTo("111-222-3344");
        assertThat(accountInfo.getUpdatedAt()).isNotNull();
        assertThat(accountInfo.getCreatedAt()).isNotNull();
    }
}