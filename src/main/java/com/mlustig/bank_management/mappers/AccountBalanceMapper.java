package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.AccountBalance;
import com.mlustig.bank_management.dto.AccountBalanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountBalanceMapper {
    AccountBalanceMapper INSTANCE = Mappers.getMapper(AccountBalanceMapper.class);

    AccountBalanceDto toDto(AccountBalance accountBalance);

    @Mapping(target = "accountBalanceId", ignore = true)
    @Mapping(target = "accountInfo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AccountBalance toDao(AccountBalanceDto accountBalanceDto);
}