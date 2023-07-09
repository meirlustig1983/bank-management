package com.mlustig.bank_management.mappers;

import com.mlustig.bank_management.dao.AccountInfo;
import com.mlustig.bank_management.dto.AccountInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountInfoMapper {
    AccountInfoMapper INSTANCE = Mappers.getMapper(AccountInfoMapper.class);

    AccountInfoDto toDto(AccountInfo accountInfo);

    @Mapping(target = "accountInfoId", ignore = true)
    @Mapping(target = "accountBalance", ignore = true)
    @Mapping(target = "accountProperties", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AccountInfo toDao(AccountInfoDto bankAccountDto);
}